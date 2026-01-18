import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class WasmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = libs.plugins.kotlin.multiplatform)

        extensions.configure<KotlinMultiplatformExtension> {

            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                binaries.executable()
                browser()
            }

            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            applyDefaultHierarchyTemplate {
                common {
                    group("nonAndroid") {
                        withJvm()
                        withWasmJs()
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

