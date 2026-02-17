import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val Project.androidConventionProvider: Provider<AndroidConvention>
    get() = provider { androidConvention }

private val Project.androidConvention: AndroidConvention
    get() = AndroidConvention(
        compileSdkVersion = libs.versions.android.compileSdk.get(),
        minSdkVersion = libs.versions.android.minSdk.get(),
        jvmTargetVersion = libs.versions.kotlin.jvmTarget.get(),
    )

private val AndroidConvention.jvmTarget: JvmTarget
    get() = JvmTarget.fromTarget(jvmTargetVersion)

private data class AndroidConvention(
    val compileSdkVersion: String,
    val minSdkVersion: String,
    val jvmTargetVersion: String,
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
                }
            }

            androidTarget {
                compilerOptions.jvmTarget.set(androidConvention.jvmTarget)
            }
        }

        val androidApplicationPlugin = libs.plugins.android.application.get()
        pluginManager.withPlugin(androidApplicationPlugin.pluginId) {
            extensions.configure<BaseAppModuleExtension> {
                compileOptions(androidConvention)
                defaultConfig(androidConvention)
            }
        }

        val androidLibraryPlugin = libs.plugins.android.library.get()
        pluginManager.withPlugin(androidLibraryPlugin.pluginId) {
            extensions.configure<LibraryExtension> {
                compileOptions(androidConvention)
                defaultConfig(androidConvention)
            }
        }
    }
}

private fun CommonExtension<*, *, *, *, *, *>.compileOptions(convention: AndroidConvention) {
    compileOptions {
        sourceCompatibility(convention.jvmTargetVersion)
        targetCompatibility(convention.jvmTargetVersion)
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
