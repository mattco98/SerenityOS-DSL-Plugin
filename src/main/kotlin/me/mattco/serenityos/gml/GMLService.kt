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
    val widgets: Map<String, Widget>

    fun lookupWidget(name: String) = widgets[name]

    fun load()
}

class GMLServiceImpl(
    private val project: Project,
) : GMLService {
    override val widgets = mutableMapOf<String, Widget>()

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
                    Json.Default.decodeFromString<List<Widget>>(file.readText()).forEach {
                        widgets[it.name] = it
                    }
                } catch (_: SerializationException) {
                }
            }

            postWidgetsLoad()
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
                val widgets = Json.Default.decodeFromString<List<Widget>>(event.file!!.text)
                ApplicationManager.getApplication().runReadAction {
                    widgets.forEach {
                        service.widgets[it.name] = it
                    }
                }

                service.postWidgetsLoad()
            } catch (_: SerializationException) {
                // If we fail to serialize the file, it is likely being edited
            }
        }
    }

    private fun postWidgetsLoad() {
        // Widget::layout is special-cased in the compiler and won't appear in the json files
        val widget = widgets["GUI::Widget"] ?: return
        if (widget.properties.none { it.name == "layout" }) {
            widgets["GUI::Widget"] = Widget(
                widget.name,
                widget.header,
                widget.inherits,
                widget.description,
                widget.properties + Property(
                    "layout",
                    "The layout of the widget",
                    "GUI::Widget",
                )
            )
        }
    }
}
