import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun KotlinCompile.configureKotlin() {
    kotlinOptions {
        jvmTarget = "11"
    }
}

@Suppress("UnstableApiUsage")
internal fun CommonExtension<*, *, *, *>.configureCommon() {
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

internal fun KotlinMultiplatformExtension.configureKotlinMultiplatform(target: Project) = target.run {
    explicitApiWarning()
    android()
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                implementation(compose.uiTooling)
                implementation(compose.ui)

                with(libs.arkivanov) {
                    implementation(decompose.extensions)
                    implementation(decompose)
                }

                with(libs.jetbrains.kotlinx) {
                    implementation(coroutinesCore)
                    implementation(datetime)
                    implementation(serializationJson)
                    implementation(serializationProperties)
                }

                implementation(libs.oolong)
            }
        }

        val commonTest by getting {
            dependencies {
                with(libs.jetbrains) {
                    implementation(kotlin.test)
                    implementation(kotlin.testAnnotations)
                    implementation(kotlin.testCommon)
                    implementation(kotlin.testJunit)
                    implementation(kotlinx.coroutinesTest)
                }
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.annotation)
                implementation(libs.androidx.coreKtx)
                implementation(libs.google.android.material)
                implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.jetbrains.kotlinx.coroutinesSwing)
            }
        }
    }
}
