import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import java.io.File

public fun Jar.dependency(provider: Provider<MinimalExternalModuleDependency>): List<FileTree> {
    val jvmRuntimeClasspath by project.configurations
    val dependency = provider.get()

    val matches = jvmRuntimeClasspath.filter {
        dependency.matches(fileTree(project, it))
    }

    return matches.map {
        fileTree(project, it)
    }
}

private fun MinimalExternalModuleDependency.matches(tree: FileTree): Boolean {
    val version = tree.firstOrNull { it.extension == "version" } ?: return false
    return version.name.contains(module.group)
}

private fun fileTree(project: Project, file: File): FileTree {
    return if (file.isDirectory) file as FileTree else project.zipTree(file)
}
