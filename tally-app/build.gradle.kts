import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.gradle.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

private object TallyAppConfig {
    const val PACKAGE_NAME = "io.ashdavies.tally"
    const val MAIN_CLASS = "${PACKAGE_NAME}.MainKt"
}

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")

    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.properties")
    id("io.ashdavies.wasm")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.paparazzi)
    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.compose.hotReload)
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
                signingConfigs.create("release") {
                    storeFile = file(properties.getProperty("storeFile"))
                    storePassword = properties.getProperty("storePassword")

                    keyAlias = properties.getProperty("keyAlias")
                    keyPassword = properties.getProperty("keyPassword")

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

        versionName = "1.0.0-$versionCode"
    }

    namespace = TallyAppConfig.PACKAGE_NAME
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
        mainClass = TallyAppConfig.MAIN_CLASS

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg)

            packageName = "Tally"
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
                outputFileName = "tally-app.js"
            }
        }
    }

    sourceSets {
        val androidJvmMain by getting {
            dependencies {
                implementation(libs.androidx.paging.common)
                implementation(libs.sqldelight.paging3.extensions)
            }
        }

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

            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)

            implementation(libs.androidx.annotation)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.back.handler)
            implementation(libs.compose.window.size)
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

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
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
            dependencies.implementation(compose.uiTooling)
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
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)

            implementation(libs.compose.window.size)
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
            dependency(projects.identityManager)
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass.set(TallyAppConfig.MAIN_CLASS)
}
