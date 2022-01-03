import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project

// https://github.com/gradle/gradle/issues/15383
val Project.libs: LibrariesForLibs get() = extensions.getByName("libs") as LibrariesForLibs
