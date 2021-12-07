import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import javax.inject.Inject

private const val DEFAULT_REGION = "europe-west1"
private const val DEFAULT_RUNTIME = "java11"

@OptIn(ExperimentalStdlibApi::class)
abstract class DeployFunctionTask @Inject constructor(
    @get:Input val allowUnauthenticated: Boolean = false,
    @get:Input val runtime: String = DEFAULT_RUNTIME,
    @get:Input val region: String = DEFAULT_REGION,
    @get:Input val triggerHttp: Boolean = true,
    @get:Input val projectName: String,
    @get:Input val entryPoint: String,
    @get:Input val source: String,
) : Exec() {
    init {
        dependsOn("shadowJar")
        description = "Publish GCP function"
        workingDir = project.buildDir
        executable = "gcloud"
        group = "deploy"

        commandLine = buildList {
            addAll(
                "gcloud", "functions", "deploy", "events",
                "--entry-point=$entryPoint",
                "--project=$projectName",
                "--runtime=$runtime",
                "--region=$region",
                "--source=$source",
            )

            if (allowUnauthenticated) add("--allow-unauthenticated")
            if (triggerHttp) add("--trigger-http")
        }
    }
}

private fun <T> MutableList<T>.addAll(vararg values: T) = addAll(values)
