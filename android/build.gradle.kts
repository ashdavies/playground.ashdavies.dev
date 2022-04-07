// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.android.build.api.dsl.VariantDimension
import org.jetbrains.compose.compose
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.compose")

    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

android {
    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"
        compileSdk = 31
        minSdk = 23

        versionCode = 1
        versionName = "1.0"

        val googleClientId by buildConfigField()
        val playgroundApiKey by buildConfigField()
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        java.srcDirs("src/$name/kotlin")
    }
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":compose-local"))
                implementation(project(":local-storage"))

                with(compose) {
                    implementation(foundation)
                    implementation(material)
                    implementation(runtime)
                    implementation(uiTooling)
                    implementation(ui)
                }

                implementation(libs.alialbaali.kamel)

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

                with(libs.ktor.client) {
                    implementation(contentNegotiation)
                    implementation(core)
                    implementation(json)
                    implementation(logging)
                }

                implementation(libs.ktor.serialization.json)
                implementation(libs.kuuuurt.multiplatformPaging)
                implementation(libs.sqlDelight.coroutinesExtensions)
            }
        }

        val commonTest by getting {
            dependencies {
                with(libs.jetbrains) {
                    implementation(kotlin.test)
                    implementation(kotlin.testJunit)
                    implementation(kotlinx.coroutinesTest)
                }
            }
        }

        val androidMain by getting {
            dependencies {
                with(libs.androidx) {
                    implementation(activityCompose)
                    implementation(activityKtx)
                    implementation(annotation)
                    implementation(coreKtx)
                    implementation(pagingCompose)
                    implementation(pagingRuntime)
                }

                with(libs.androidx.lifecycle) {
                    implementation(viewModelCompose)
                    implementation(viewModelKtx)
                }

                with(libs.google.accompanist) {
                    implementation(flowlayout)
                    implementation(insets)
                    implementation(insetsUi)
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                    implementation(systemuicontroller)
                }

                implementation(libs.google.android.material)

                with(libs.google.firebase) {
                    implementation(analytics)
                    implementation(commonKtx)
                }

                implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
                implementation(libs.ktor.client.cio)
                implementation(libs.slf4j)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                with(libs.androidx) {
                    implementation(pagingCompose)
                    implementation(pagingRuntime)
                }

                implementation(libs.jetbrains.kotlinx.coroutinesSwing)
                implementation(libs.ktor.client.cio)
                implementation(libs.slf4j)
            }
        }
    }
}

fun VariantDimension.buildConfigField(): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>> {
    return SystemProperty { name, value -> buildConfigField("String", name, "\"$value\"") }
}
