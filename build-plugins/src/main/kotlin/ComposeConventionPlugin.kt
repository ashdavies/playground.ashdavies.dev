import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.compose.compiler)
        plugins.apply(libs.plugins.jetbrains.compose)

        pluginManager.withPlugin(libs.plugins.kotlin.multiplatform) {
            configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    implementation(libs.compose.runtime)
                }

                targets.withType<KotlinJsIrTarget>().configureEach {
                    binaries.executable()
                }
            }
        }
    }
}
