import eu.appcom.gradle.VersioningExtension
import org.gradle.api.Project

val Project.versioning: VersioningExtension
    get() = extensions.getByName("versioning") as VersioningExtension

fun Project.versioning(configure: VersioningExtension.() -> Unit) {
    return extensions.configure("versioning", configure)
}
