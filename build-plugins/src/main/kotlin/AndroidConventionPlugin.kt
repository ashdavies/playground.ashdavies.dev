import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class AndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = libs.plugins.kotlin.multiplatform)

        val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilerOptions.jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
            }
        }

        project.commonExtension {
            compileOptions {
                sourceCompatibility(jvmTargetVersion)
                targetCompatibility(jvmTargetVersion)
            }

            defaultConfig {
                val compileSdkVersion = libs.versions.android.compileSdk.get()
                compileSdk = compileSdkVersion.toInt()

                val minSdkVersion = libs.versions.android.minSdk.get()
                minSdk = minSdkVersion.toInt()
            }

            sourceSets.configureEach {
                manifest.srcFile("src/androidMain/AndroidManifest.xml")
            }
        }
    }
}
