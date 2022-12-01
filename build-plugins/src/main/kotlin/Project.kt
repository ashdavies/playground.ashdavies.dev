import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val KotlinMultiplatformExtension.compose: ComposePlugin.Dependencies
    get() = ComposePlugin.Dependencies

/**
 * In a similar way to [#15382](https://github.com/gradle/gradle/issues/15382), we want to make the
 * version catalogs accessible to precompiled script plugins. Naively, one might think that it's
 * easier to do because precompiled script plugins are applied to the "main" build scripts, but
 * this isn't necessarily the case.
 *
 * @see [#15383](https://github.com/gradle/gradle/issues/15383)
 */
internal val Project.libs: LibrariesForLibs
    get() = extensions.getByName<LibrariesForLibs>("libs")
