import com.google.cloud.tools.jib.api.Containerizer
import com.google.cloud.tools.jib.api.ImageReference
import com.google.cloud.tools.jib.api.Jib
import com.google.cloud.tools.jib.api.RegistryImage
import com.google.cloud.tools.jib.api.buildplan.AbsoluteUnixPath
import com.google.cloud.tools.jib.frontend.CredentialRetrieverFactory
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

private const val BASE_IMAGE_REFERENCE = "eclipse-temurin:17.0.14_7-jre"

public abstract class BuildImageTask : DefaultTask() {

    @get:Input
    public abstract val image: Property<String>

    @get:InputFiles
    public abstract val classpathFiles: ConfigurableFileCollection

    @get:Input
    public abstract val mainClass: Property<String>

    @TaskAction
    public fun build() {
        val dockerCredentialRetriever = CredentialRetrieverFactory
            .forImage(ImageReference.parse(image.get())) { }
            .dockerConfig()

        val googleCredentialRetriever = CredentialRetrieverFactory
            .forImage(ImageReference.parse(image.get())) { }
            .googleApplicationDefaultCredentials()

        val registryImage = RegistryImage
            .named(image.get())
            .addCredentialRetriever(dockerCredentialRetriever)
            .addCredentialRetriever(googleCredentialRetriever)

        Jib.from(BASE_IMAGE_REFERENCE)
            .addLayer(classpathFiles.files.map(File::toPath), AbsoluteUnixPath.get("/libs"))
            .setEntrypoint("java", "-cp", "/libs/*", mainClass.get())
            .containerize(Containerizer.to(registryImage))
    }
}
