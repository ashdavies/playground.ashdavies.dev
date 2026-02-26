import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.gradle.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

private object ConferenceAppConfig {
    const val APPLICATION_NAME = "dev.ashdavies.playground"
    const val PACKAGE_NAME = "dev.ashdavies.playground"
    const val MAIN_CLASS = "${PACKAGE_NAME}.MainKt"
}

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")

    id("dev.ashdavies.android")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.properties")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.paparazzi)
    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.zac.metro)
}

android {
    val androidTest by sourceSets.getting {
        res.srcDirs("src/androidMain/res")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            val keystoreProperties = localPropertyProvider("keystore.properties")

            signingConfig = keystoreProperties.map { properties ->
                signingConfigs.maybeCreate("release").apply {
                    storeFile = file(properties.getProperty("store.file"))
                    storePassword = properties.getProperty("store.password")

                    keyAlias = properties.getProperty("key.alias")
                    keyPassword = properties.getProperty("key.password")

                    enableV3Signing = true
                    enableV4Signing = true
                }
            }.orNull

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    defaultConfig {
        val androidApiKey by stringProperty { _, value ->
            manifestPlaceholders["ANDROID_API_KEY"] = "$value"
        }

        val versionCode by stringProperty { _, value ->
            versionCode = value?.toInt() ?: 1
        }

        val versionName by stringProperty { _, value ->
            versionName = value ?: "0.0.0-SNAPSHOT"
        }
    }

    namespace = ConferenceAppConfig.APPLICATION_NAME
}

buildConfig {
    val androidApiKey by stringProperty(::buildConfigField)
    val browserApiKey by stringProperty(::buildConfigField)

    className.set("BuildConfig")
    packageName.set(android.namespace)
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

compose.desktop {
    application {
        mainClass = ConferenceAppConfig.MAIN_CLASS

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg)

            packageName = "Conference App"
            packageVersion = "1.0.0"
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "conference-app.js"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.asgService)
            implementation(projects.cloudCommon)
            implementation(projects.composeMaterial)
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.identityManager)
            implementation(projects.kotlinDelegates)
            implementation(projects.mapsRouting)
            implementation(projects.pagingMultiplatform)
            implementation(projects.placeholderHighlight)
            implementation(projects.platformScaffold)
            implementation(projects.platformSupport)
            implementation(projects.remoteConfig)
            implementation(projects.sqlCommon)
            implementation(projects.sqlDriver)

            implementation(libs.androidx.annotation)
            implementation(libs.androidx.window.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.back.handler)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.io.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.http)
            implementation(libs.ktor.io)
            implementation(libs.slack.circuit.foundation)
            implementation(libs.slack.circuit.overlay)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.app.cash.turbine)
            implementation(libs.kotlinx.coroutines.test)
        }

        val androidJvmMain by getting {
            dependencies {
                implementation(libs.androidx.paging.common)
                implementation(libs.sqldelight.paging3.extensions)
            }
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.startup.runtime)
            implementation(libs.google.accompanist.permissions)
            implementation(libs.google.android.location)
            implementation(libs.google.android.material)
            implementation(libs.google.maps.android.compose)
            implementation(libs.google.maps.android.utils)

            implementation(dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.appcheck.playintegrity)
            implementation(libs.google.firebase.crashlytics)

            implementation(libs.kotlinx.coroutines.play.services)
            implementation(libs.slack.circuit.overlay)
        }

        val androidDebug by registering {
            dependencies {
                implementation(libs.google.firebase.appcheck.debug)
                implementation(libs.compose.uiTooling)
            }
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.keyNavigation)

            runtimeOnly(libs.kotlinx.coroutines.swing)
            runtimeOnly(libs.slf4j.simple)
        }

        jvmTest.dependencies {
            implementation(libs.androidx.paging.testing)
            implementation(libs.sqldelight.sqlite.driver)
        }

        wasmJsMain.dependencies {
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)

            implementation(libs.slack.circuit.foundation)
            implementation(libs.slack.circuit.overlay)
        }
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName = android.namespace
            generateAsync = true

            dialect(libs.sqldelight.sqlite.dialect)
            dependency(project(":identity-manager"))
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass.set(ConferenceAppConfig.MAIN_CLASS)
}
