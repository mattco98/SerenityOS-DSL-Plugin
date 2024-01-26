package me.mattco.serenityos.common

import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

// TODO: Dependent on folder name?
val Project.isSerenity: Boolean
    get() = name == "SerenityOS"

val Project.userlandDirectory: VirtualFile?
    get() = getBaseDirectories().singleOrNull()?.findChild("Userland")
