import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val jvmTargetString = libs.versions.kotlin.jvmTarget.get()
        val jvmTarget = JavaVersion.toVersion(jvmTargetString)

        configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate {
                common {
                    group("androidJvm") {
                        withAndroidTarget()
                        withJvm()
                    }
                }
            }
        }

        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = jvmTarget
            targetCompatibility = jvmTarget
        }
    }
}
