import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

public class KotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(libs.plugins.kotlin.serialization)
        plugins.apply(libs.plugins.kotlin.multiplatform)

        apply<DetektPlugin>()
        apply<KtlintPlugin>()

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()
            jvm()
        }

        dependencies.add("detektPlugins", libs.detekt.compose)

        extensions.configure<DetektExtension> {
            config.setFrom(rootProject.file("detekt-config.yml"))
            parallel = true
            buildUponDefaultConfig = true

            val detektPlugin = libs.plugins.detekt.get()
            toolVersion = "${detektPlugin.version}"
        }

        tasks.withType(KotlinCompile::class.java).configureEach {
            compilerOptions {
                val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()
                jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
            }
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
