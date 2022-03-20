import com.android.build.gradle.LibraryExtension
import org.gradle.api.Incubating
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class MultiplatformLibrary : Plugin<Project> {

    override fun apply(target: Project): Unit = target.run {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.compose")
        plugins.apply("org.jetbrains.kotlin.multiplatform")
        plugins.apply("org.jetbrains.kotlin.plugin.serialization")

        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            extensions.configure<KotlinMultiplatformExtension> { configure(target) }
            extensions.configure(LibraryExtension::configure)
        }

        tasks.withType(KotlinCompile::configure)
    }
}

private fun KotlinCompile.configure() {
    kotlinOptions {
        jvmTarget = "11"
    }
}

private fun KotlinMultiplatformExtension.configure(target: Project) {
    explicitApiWarning()
    android()
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                with(target.libs.jetbrains.kotlinx) {
                    implementation(coroutinesCore)
                    implementation(serializationJson)
                }

                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                implementation(compose.uiTooling)
                implementation(compose.ui)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(target.libs.jetbrains.kotlin.testAnnotations)
                implementation(target.libs.jetbrains.kotlin.testCommon)
            }
        }

        val androidMain by getting {
            dependencies {
                with(target.libs.jetbrains.kotlinx) {
                    implementation(coroutinesAndroid)
                }
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(target.libs.jetbrains.kotlinx.coroutinesJdk)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

@Suppress("UnstableApiUsage")
private fun LibraryExtension.configure() {
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        compileSdk = 31
        minSdk = 21
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
