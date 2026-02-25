import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal class DefaultConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply("com.android.library")
        plugins.apply("dev.ashdavies.android")
        plugins.apply("dev.ashdavies.kotlin")

        apply<JvmConventionPlugin>()
        apply<WasmConventionPlugin>()
    }
}
