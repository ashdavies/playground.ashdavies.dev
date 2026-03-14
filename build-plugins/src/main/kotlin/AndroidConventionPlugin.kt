import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val Project.androidConventionProvider: Provider<AndroidConvention>
    get() = provider { androidConvention }

private val Project.androidConvention: AndroidConvention
    get() = AndroidConvention(
        compileSdkVersion = libs.versions.android.compileSdk.get(),
        minSdkVersion = libs.versions.android.minSdk.get(),
    )

private data class AndroidConvention(
    val compileSdkVersion: String,
    val minSdkVersion: String,
)

internal class AndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.kotlin.multiplatform)

        val androidConvention = androidConventionProvider.get()
        extensions.configure<KotlinMultiplatformExtension> {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            applyDefaultHierarchyTemplate {
                common {
                    group("androidJvm") {
                        withAndroidTarget()
                        withJvm()
                    }

                    group("nonAndroid") {
                        withJvm()
                        withWasmJs()
                    }
                }
            }
        }

        val androidApplicationPlugin = libs.plugins.android.application.get()
        pluginManager.withPlugin(androidApplicationPlugin.pluginId) {
            extensions.configure<BaseAppModuleExtension> {
                defaultConfig(androidConvention)
            }
        }

        val androidLibraryPlugin = libs.plugins.android.library.get()
        pluginManager.withPlugin(androidLibraryPlugin.pluginId) {
            extensions.configure<LibraryExtension> {
                defaultConfig(androidConvention)
            }
        }
    }
}

private fun CommonExtension<*, *, *, *, *, *>.defaultConfig(convention: AndroidConvention) {
    defaultConfig {
        compileSdk = convention
            .compileSdkVersion
            .toInt()

        minSdk = convention
            .minSdkVersion
            .toInt()
    }
}
