import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with<_, Unit>(target) {
        plugins.apply(libs.plugins.kotlin.multiplatform)
        plugins.apply(libs.plugins.kotlin.serialization)

        extensions.configure<KotlinMultiplatformExtension> {
            jvm()
        }
    }
}
