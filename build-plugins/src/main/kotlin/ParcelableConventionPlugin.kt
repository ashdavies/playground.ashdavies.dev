import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class ParcelableConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = libs.plugins.android.application)
        apply(plugin = libs.plugins.kotlin.multiplatform)
        apply(plugin = "kotlin-parcelize")

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilerOptions {
                    val additionalAnnotation = "org.jetbrains.kotlin.parcelize:additionalAnnotation"
                    val parcelizeAnnotation = "dev.ashdavies.parcelable.Parcelize"

                    freeCompilerArgs.addAll("-P", "plugin:$additionalAnnotation=$parcelizeAnnotation")
                }
            }

            sourceSets.commonMain.dependencies {
                implementation(project(":parcelable-support"))
            }
        }
    }
}
