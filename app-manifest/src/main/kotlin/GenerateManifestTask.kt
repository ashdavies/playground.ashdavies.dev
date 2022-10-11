import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

@CacheableTask
internal abstract class GenerateManifestTask @Inject constructor(factory: ObjectFactory) : DefaultTask() {

    private val srcFile: RegularFileProperty = factory.fileProperty { ManifestFileProducer(project) }

    @TaskAction
    fun generateManifest() {
        AndroidManifest(srcFile.get().asFile).generate()
    }
}

private fun ObjectFactory.fileProperty(value: () -> File) = fileProperty().apply { set(value()) }
