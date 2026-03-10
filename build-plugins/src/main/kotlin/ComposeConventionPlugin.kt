import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.compose.compiler)
        plugins.apply(libs.plugins.jetbrains.compose)
        plugins.apply(libs.plugins.kotlin.multiplatform)

        configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.compose.runtime)
            }
        }
    }
}
