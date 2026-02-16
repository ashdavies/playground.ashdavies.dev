import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val Project.androidLibraryConvention: AndroidLibraryConvention
    get() = AndroidLibraryConvention(
        compileSdkVersion = libs.versions.android.compileSdk.get(),
        minSdkVersion = libs.versions.android.minSdk.get(),
        jvmTargetVersion = libs.versions.kotlin.jvmTarget.get(),
    )

private val AndroidLibraryConvention.jvmTarget: JvmTarget
    get() = JvmTarget.fromTarget(jvmTargetVersion)

private data class AndroidLibraryConvention(
    val compileSdkVersion: String,
    val minSdkVersion: String,
    val jvmTargetVersion: String,
)

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.kotlin.multiplatform)

        val androidLibraryConvention = androidLibraryConvention
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
                compilerOptions.jvmTarget.set(androidLibraryConvention.jvmTarget)
            }
        }

        val androidApplicationPlugin = libs.plugins.android.application.get()
        pluginManager.withPlugin(androidApplicationPlugin.pluginId) {
            extensions.configure<BaseAppModuleExtension> {
                compileOptions(androidLibraryConvention.jvmTarget)
                defaultConfig(androidLibraryConvention)
            }
        }

        val androidLibraryPlugin = libs.plugins.android.library.get()
        pluginManager.withPlugin(androidLibraryPlugin.pluginId) {
            extensions.configure<LibraryExtension> {
                compileOptions(androidLibraryConvention.jvmTarget)
                defaultConfig(androidLibraryConvention)
            }
        }
    }
}

private fun CommonExtension<*, *, *, *, *, *>.compileOptions(version: JvmTarget) {
    compileOptions {
        sourceCompatibility(version)
        targetCompatibility(version)
    }
}

private fun CommonExtension<*, *, *, *, *, *>.defaultConfig(convention: AndroidLibraryConvention) {
    defaultConfig {
        compileSdk = convention
            .compileSdkVersion
            .toInt()

        minSdk = convention
            .minSdkVersion
            .toInt()
    }
}
