import org.gradle.api.Plugin
import org.gradle.api.Project

internal class FirebaseHostingPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        tasks.register("firebaseDeploy", FirebaseDeployTask::class.java)
    }
}