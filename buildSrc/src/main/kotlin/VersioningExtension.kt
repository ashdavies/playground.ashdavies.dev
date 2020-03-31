import org.gradle.api.Project
import eu.appcom.gradle.VersioningExtension

val Project.versioning: VersioningExtension
  get() = extensions.getByName("versioning") as VersioningExtension

fun Project.versioning(configure: VersioningExtension.() -> Unit) {
  return extensions.configure("versioning", configure)
}