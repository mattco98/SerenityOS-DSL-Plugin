import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    idea
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.16.0"
    id("org.jetbrains.grammarkit") version "2022.3.2"
    alias(libs.plugins.changelog)
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
}

dependencies {
}

intellij {
    pluginName = properties("pluginName")
    version = properties("platformVersion")
    type = properties("platformType")
    plugins = properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) }
}

kotlin {
    @Suppress("UnstableApiUsage")
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.JETBRAINS
    }
}

changelog {
    groups.empty()
    repositoryUrl = properties("pluginRepositoryUrl")
}

sourceSets["main"].java.srcDirs("src/main/gen")

tasks {
    properties("javaVersion").get().let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = it
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xjvm-default=all")
            }
        }
    }

    generateLexer.configure { enabled = false }
    generateParser.configure { enabled = false }

    task<GenerateLexerTask>("generateIDLLexer") {
        group = "grammarkit"
        sourceFile.set(file("src/main/resources/grammar/SerenityOS IDL.flex"))
        targetDir.set("src/main/gen/me/mattco/serenityos/idl")
        targetClass.set("IDLLexer.java")
        purgeOldFiles.set(true)
        skeleton
    }

    task<GenerateParserTask>("generateIDLParser") {
        group = "grammarkit"
        sourceFile.set(file("src/main/resources/grammar/SerenityOS IDL.bnf"))
        targetRoot.set("src/main/gen")
        pathToParser.set("me/mattco/serenityos/idl/IDLParser.java")
        pathToPsiRoot.set("me/mattco/serenityos/psi")
        purgeOldFiles.set(true)
    }

    task("generateAll") {
        group = "grammarkit"
        dependsOn("generateIDLLexer", "generateIDLParser")
    }

    compileKotlin {
        dependsOn("generateAll")

        kotlinOptions {
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    patchPluginXml {
        version = properties("pluginVersion")
        sinceBuild = properties("pluginSinceBuild")
        untilBuild = properties("pluginUntilBuild")

        pluginDescription = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with (it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = properties("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }
    }

    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token = environment("PUBLISH_TOKEN")
        channels = properties("pluginVersion").map { listOf(it.split('-').getOrElse(1) { "default" }.split('.').first()) }
    }
}
