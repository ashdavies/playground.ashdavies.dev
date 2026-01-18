import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeBuildConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = libs.plugins.jetbrains.compose)
        apply(plugin = libs.plugins.compose.compiler)
        apply(plugin = libs.plugins.kotlin.multiplatform)


        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:${ComposeBuildConfig.composeVersion}")
                // implementation(compose.runtime)
            }

            jvm()
        }
    }
}
