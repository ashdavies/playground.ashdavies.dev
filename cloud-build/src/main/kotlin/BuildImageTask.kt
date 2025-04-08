import com.google.cloud.tools.jib.api.Containerizer
import com.google.cloud.tools.jib.api.DockerDaemonImage
import com.google.cloud.tools.jib.api.Jib
import com.google.cloud.tools.jib.api.buildplan.AbsoluteUnixPath
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

private const val BASE_IMAGE_REFERENCE = "eclipse-temurin:17.0.14_7-jre"

public abstract class BuildImageTask : DefaultTask() {

    @get:Input
    public abstract val image: Property<String>

    @get:InputFile
    public abstract val jarFile: Property<File>

    @get:Input
    public abstract val mainClass: Property<String>

    @TaskAction
    public fun build() {
        Jib.from(BASE_IMAGE_REFERENCE)
            .addLayer(listOf(jarFile.get().toPath()), AbsoluteUnixPath.get("/"))
            .setEntrypoint("java", "-cp", mainClass.get())
            .containerize(Containerizer.to(DockerDaemonImage.named(image.get())))
    }
}
