import com.android.build.api.withAndroid
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jlleitschuh.gradle.ktlint.KtlintExtension

public val NamedDomainObjectContainer<KotlinSourceSet>.androidJvmMain: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named("androidJvmMain")

public class KotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.withPlugin(libs.plugins.kotlin.multiplatform) {
            plugins.apply(libs.plugins.kotlin.serialization)
            plugins.apply(libs.plugins.kotlin.multiplatform)

            plugins.apply(libs.plugins.detekt)
            plugins.apply(libs.plugins.ktlint)

            extensions.configure<KotlinMultiplatformExtension> {
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                applyDefaultHierarchyTemplate {
                    common {
                        group("androidJvm") {
                            @Suppress("UnstableApiUsage")
                            withAndroid()
                            withJvm()
                        }

                        group("jvmWasmJs") {
                            withJvm()
                            withWasmJs()
                        }
                    }
                }

                compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
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
}
