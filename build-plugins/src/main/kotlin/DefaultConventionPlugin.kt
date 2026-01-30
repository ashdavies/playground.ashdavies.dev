import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal class DefaultConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.android.library")
        apply(plugin = "dev.ashdavies.android")
        apply(plugin = "dev.ashdavies.kotlin")

        apply<JvmConventionPlugin>()
        apply<WasmConventionPlugin>()
    }
}
