import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

public abstract class FirebaseDeployTask : DefaultTask() {

    @get:Input
    public abstract val projectId: Property<String>

    @get:Input
    public abstract val siteId: Property<String>

    @get:InputDirectory
    public abstract val publicDir: DirectoryProperty

    @TaskAction
    public fun deploy() {
        println("FirebaseDeployTask: project = ${projectId.get()}, site = ${siteId.get()}, directory = ${publicDir.get().asFile.absolutePath}")
    }
}