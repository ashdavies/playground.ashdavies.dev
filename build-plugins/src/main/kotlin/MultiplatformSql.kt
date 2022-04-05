import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class MultiplatformSql : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("com.squareup.sqldelight")
        plugins.apply("org.jetbrains.kotlin.multiplatform")

        extensions.configure<KotlinMultiplatformExtension> { configure(target) }
    }
}

private fun KotlinMultiplatformExtension.configure(target: Project) {
    explicitApiWarning()
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(target.libs.sqlDelight) {
                    implementation(coroutinesExtensions)
                    implementation(runtime)
                }
            }
        }

        val androidMain by getting {
            dependencies {
                with(target.libs) {
                    implementation(requery.sqliteAndroid)
                    implementation(sqlDelight.androidDriver)
                }
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(target.libs.sqlDelight.sqliteDriver)
            }
        }
    }
}
