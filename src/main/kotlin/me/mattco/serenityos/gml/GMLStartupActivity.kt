package me.mattco.serenityos.gml

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class GMLStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        project.service<GMLService>().load()
    }
}
