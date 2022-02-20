import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal abstract class ProjectCommon : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            extensions.configure<KotlinMultiplatformExtension> {
                explicitApiWarning()

                sourceSets {
                    all {
                        languageSettings.optIn("kotlin.RequiresOptIn")
                    }

                    val commonTest by getting {
                        dependencies {
                            implementation(libs.jetbrains.kotlin.testAnnotations)
                            implementation(libs.jetbrains.kotlin.testCommon)
                        }
                    }
                }
            }
        }
    }
}
