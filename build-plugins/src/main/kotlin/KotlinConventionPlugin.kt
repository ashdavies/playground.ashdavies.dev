import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

internal class KotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with<Project, Unit>(target) {
        apply(plugin = libs.plugins.kotlin.serialization)
        apply(plugin = libs.plugins.kotlin.multiplatform)
        apply(plugin = libs.plugins.detekt)
        apply(plugin = libs.plugins.ktlint)

        dependencies {
            add("detektPlugins", libs.detekt.compose)
        }

        extensions.configure<DetektExtension> {
            config.setFrom(rootProject.file("detekt-config.yml"))
            parallel = true
            buildUponDefaultConfig = true

            val detektPlugin = libs.plugins.detekt.get()
            toolVersion = "${detektPlugin.version}"
        }

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()
            jvm()

            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            applyDefaultHierarchyTemplate {
                common {
                    group("androidJvm") {
                        withAndroidTarget()
                        withJvm()
                    }
                }
            }
        }

        extensions.configure<KtlintExtension> {
            filter {
                exclude { "generated" in it.file.path }
            }

            val ktlintBom = libs.pinterest.ktlint.bom.get()
            version.set(ktlintBom.version)
        }

        tasks.withType<KotlinCompile> {
            compilerOptions {
                val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()
                jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
            }
        }
    }
}
