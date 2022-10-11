import org.gradle.api.Project
import java.io.File

private const val GENERATED_MANIFEST_PATH = "generated/manifests/AndroidManifest.xml"

internal object ManifestFileProducer : (Project) -> File {
    override fun invoke(project: Project): File {
        return File(project.buildDir, GENERATED_MANIFEST_PATH)
    }
}
