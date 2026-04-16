import com.android.build.api.dsl.KotlinMultiplatformAndroidCompilation
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.withAndroid
import org.gradle.api.Incubating
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.android.library)
        plugins.apply(libs.plugins.kotlin.multiplatform)

        extensions.configure<KotlinMultiplatformExtension> {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            applyDefaultHierarchyTemplate {
                common {
                    group("androidJvm") {
                        @Suppress("UnstableApiUsage")
                        withAndroid()
                        withJvm()
                    }

                    group("nonAndroid") {
                        withJvm()
                        withWasmJs()
                    }
                }
            }
        }

        extensions.configure<KotlinMultiplatformExtension> {
            configure<KotlinMultiplatformAndroidLibraryTarget> {
                val androidProjectConvention = androidProjectConventionProvider.get()

                compileSdk = androidProjectConvention
                    .compileSdkVersion
                    .toInt()

                minSdk = androidProjectConvention
                    .minSdkVersion
                    .toInt()
            }
        }
    }
}
