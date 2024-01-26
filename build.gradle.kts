import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    idea
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.grammarkit)
    alias(libs.plugins.gradleIntelliJPlugin)
    alias(libs.plugins.changelog)
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.kotlinSerialization)
}

intellij {
    pluginName = properties("pluginName")
    version = properties("platformVersion")
    type = properties("platformType")
    plugins = properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) }
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

    val supportedDSLs = listOf("GML", "IDL", "IPC")

    for (dsl in supportedDSLs) {
        task<GenerateLexerTask>("generate${dsl}Lexer") {
            group = "grammarkit"
            sourceFile.set(file("src/main/resources/grammar/SerenityOS ${dsl}.flex"))
            targetDir.set("src/main/gen/me/mattco/serenityos/${dsl.lowercase()}")
            targetClass.set("${dsl}Lexer.java")
            purgeOldFiles.set(true)
            skeleton
        }

        task<GenerateParserTask>("generate${dsl}Parser") {
            group = "grammarkit"
            sourceFile.set(file("src/main/resources/grammar/SerenityOS ${dsl}.bnf"))
            targetRoot.set("src/main/gen")
            pathToParser.set("me/mattco/serenityos/${dsl.lowercase()}/${dsl}Parser.java")
            pathToPsiRoot.set("me/mattco/serenityos/${dsl.lowercase()}/psi")
            purgeOldFiles.set(true)
        }
    }

    task("generateAll") {
        group = "grammarkit"
        supportedDSLs.forEach { dependsOn("generate${it}Lexer", "generate${it}Parser") }
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

    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
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

    publishPlugin {
        dependsOn("patchChangelog")
        token = environment("PUBLISH_TOKEN")
        channels = properties("pluginVersion").map { listOf(it.split('-').getOrElse(1) { "default" }.split('.').first()) }
    }
}
