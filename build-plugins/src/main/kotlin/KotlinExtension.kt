import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.file.FileTree
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

public object Playground {

    const val jvmTarget = "11"

    val freeCompilerArgs = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xallow-result-return-type",
        "-Xmulti-platform"
    )
}

public fun Jar.dependency(provider: Provider<MinimalExternalModuleDependency>): List<FileTree> {
    fun MinimalExternalModuleDependency.matches(tree: FileTree): Boolean {
        val version = tree.firstOrNull { it.extension == "version" } ?: return false
        return version.name.contains(module.group)
    }

    fun fileTree(project: Project, file: File): FileTree {
        return if (file.isDirectory) file as FileTree else project.zipTree(file)
    }

    val jvmRuntimeClasspath by project.configurations
    val dependency = provider.get()

    val matches = jvmRuntimeClasspath.filter {
        dependency.matches(fileTree(project, it))
    }

    return matches.map {
        fileTree(project, it)
    }
}

public fun NamedDomainObjectCollection<KotlinSourceSet>.dependencies(
    configure: KotlinDependencyHandler.() -> Unit
) = getting { dependencies(configure) }

public inline fun <reified T> ExtensionContainer.withExtension(configure: T.() -> Unit) {
    findByType(T::class.java)?.configure()
}
