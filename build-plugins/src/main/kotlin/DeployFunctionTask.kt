import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input

public abstract class DeployFunctionTask : Exec() {

    @Input lateinit var entryPoint: String
    @Input lateinit var function: String

    @Input var projectId = "playground-1a136"
    @Input var allowUnauthenticated = false
    @Input var region = "europe-west1"
    @Input var source = "playground"
    @Input var runtime = "java11"
    @Input var trigger = "http"

    init {
        with(project) {
            dependsOn(tasks.named("shadowJar"))
            workingDir = buildDir
            group = "deploy"
        }
    }

    override fun exec() {
        commandLine = mutableListOf(
            "gcloud", "functions", "deploy", function,
            "--entry-point=$entryPoint",
            "--project=$projectId",
            "--runtime=$runtime",
            "--trigger-$trigger",
            "--region=$region",
            "--source=$source",
        )

        if (allowUnauthenticated) {
            commandLine.add("--allow-unauthenticated")
        }

        super.exec()
    }
}
