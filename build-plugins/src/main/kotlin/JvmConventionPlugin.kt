import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.kotlin.multiplatform) {
            extensions.configure<KotlinMultiplatformExtension> {
                jvm()
            }
        }
    }
}
