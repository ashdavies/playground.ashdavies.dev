import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

public class KotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(libs.plugins.kotlin.serialization)
        plugins.apply(libs.plugins.kotlin.multiplatform)

        plugins.apply(libs.plugins.detekt)
        plugins.apply(libs.plugins.ktlint)

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()
        }

        dependencies.add("detektPlugins", libs.detekt.compose)

        extensions.configure<DetektExtension> {
            config.setFrom(rootProject.file("detekt-config.yml"))
            parallel = true
            buildUponDefaultConfig = true

            val detektPlugin = libs.plugins.detekt.get()
            toolVersion = "${detektPlugin.version}"
        }

        extensions.configure<KtlintExtension> {
            filter {
                exclude { "generated" in it.file.path }
            }

            val ktlintBom = libs.pinterest.ktlint.bom.get()
            version.set(ktlintBom.version)
        }
    }
}
