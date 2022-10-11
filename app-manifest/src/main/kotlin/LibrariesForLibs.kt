import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName

/*
 * Make version catalogs accessible from precompiled script plugins
 * https://github.com/gradle/gradle/issues/15383
 */
internal val Project.libs: LibrariesForLibs
    get() = extensions.getByName<LibrariesForLibs>("libs")
