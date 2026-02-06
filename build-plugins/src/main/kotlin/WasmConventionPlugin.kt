import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)
internal class WasmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = libs.plugins.kotlin.multiplatform)

        configure<KotlinMultiplatformExtension> {
            wasmJs {
                binaries.executable()
                browser()
            }

            applyDefaultHierarchyTemplate {
                common {
                    val androidLibraryPlugin = libs.plugins.android.library.get()
                    if (pluginManager.hasPlugin(androidLibraryPlugin.pluginId)) {
                        group("nonAndroid") {
                            withJvm()
                            withWasmJs()
                        }
                    }

                    group("wasm") {
                        withWasmJs()
                        withWasmWasi()
                    }
                }
            }
        }
    }
}
