package me.mattco.serenityos.gml

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.readText
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScopes
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import me.mattco.serenityos.common.DSLTreeChangeListener
import me.mattco.serenityos.common.isSerenity
import me.mattco.serenityos.common.userlandDirectory
import kotlin.collections.set

interface GMLService {
    val components: Map<String, Component>

    fun lookupComponent(name: String) = components[normalizeName(name)]

    fun load()

    companion object {
        fun normalizeName(name: String) = when (name) {
            "GUI::HorizontalSplitter", "GUI::VerticalSplitter" -> "GUI::Splitter"
            "GUI::HorizontalSeparator", "GUI::VerticalSeparator" -> "GUI::SeparatorWidget"
            "GUI::HorizontalBoxLayout", "GUI::VerticalBoxLayout" -> "GUI::BoxLayout"
            "GUI::HorizontalProgressbar", "GUI::VerticalProgressbar" -> "GUI::Progressbar"
            "GUI::DialogButton" -> "GUI::Button"
            "GUI::PasswordBox" -> "GUI::TextBox"
            else -> name
        }
    }
}

class GMLServiceImpl(
    private val project: Project,
) : GMLService {
    override val components = mutableMapOf<String, Component>()

    override fun load() {
        if (!project.isSerenity)
            return

        ApplicationManager.getApplication().runReadAction {
            val propertyFiles = FilenameIndex.getVirtualFilesByName(
                "property-definitions.json",
                GlobalSearchScopes.directoryScope(
                    project,
                    project.userlandDirectory ?: return@runReadAction,
                    true,
                ),
            )

            for (file in propertyFiles) {
                try {
                    Json.Default.decodeFromString<List<Component>>(file.readText()).forEach {
                        components[it.name] = it
                    }
                } catch (_: SerializationException) {
                }
            }

            postComponentLoad()
        }
    }

    init {
        PsiManager.getInstance(project).addPsiTreeChangeListener(TreeChangeListener(this)) {}
    }

    class TreeChangeListener(private val service: GMLServiceImpl) : DSLTreeChangeListener() {
        override fun childrenChanged(event: PsiTreeChangeEvent) {
            if (event.file?.name != "property-definitions.json")
                return

            try {
                val components = Json.Default.decodeFromString<List<Component>>(event.file!!.text)
                ApplicationManager.getApplication().runReadAction {
                    components.forEach {
                        service.components[it.name] = it
                    }
                }

                service.postComponentLoad()
            } catch (_: SerializationException) {
                // If we fail to serialize the file, it is likely being edited
            }
        }
    }

    private fun postComponentLoad() {
        // Widget::layout is special-cased in the compiler and won't appear in the json files
        val widgetComponent = components["GUI::Widget"] ?: return
        if (widgetComponent.properties.none { it.name == "layout" }) {
            components["GUI::Widget"] = Component(
                widgetComponent.name,
                widgetComponent.header,
                widgetComponent.inherits,
                widgetComponent.description,
                widgetComponent.properties + Property(
                    "layout",
                    "The layout of the component",
                    "GUI::Component",
                )
            )
        }
    }
}
