import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/*
 * Make version catalogs accessible from precompiled script plugins
 * https://github.com/gradle/gradle/issues/15383
 */
val Project.libs: LibrariesForLibs
    get() = extensions.getByName<LibrariesForLibs>("libs")

val KotlinMultiplatformExtension.compose: ComposePlugin.Dependencies
    get() = ComposePlugin.Dependencies
