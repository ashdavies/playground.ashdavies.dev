import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.gradle.ComposeHotRun

private object ConferenceAppConfig {
    const val APPLICATION_NAME = "dev.ashdavies.playground"
    const val PACKAGE_NAME = "dev.ashdavies.playground"
    const val MAIN_CLASS = "${PACKAGE_NAME}.MainKt"
}

plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.compose")
    id("dev.ashdavies.integration")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.properties")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.metro)
}

kotlin {
    android.namespace = ConferenceAppConfig.APPLICATION_NAME
}

buildConfig {
    buildConfigField("API_KEY", expect<String?>(null))
    buildConfigField("APP_ID", expect<String?>(null))

    buildConfigField("PLAYGROUND_BASE_URL", stringPropertyOrNull("playgroundBaseUrl"))
    buildConfigField("GOOGLE_CLOUD_PROJECT", stringPropertyOrNull("googleCloudProject"))
    buildConfigField("VERSION_NAME", stringPropertyOrNull("versionName") ?: "UNKNOWN")

    sourceSets.named("androidMain") {
        buildConfigField("API_KEY", stringPropertyOrNull("androidApiKey"))
        buildConfigField("APP_ID", stringPropertyOrNull("androidAppId"))
    }

    sourceSets.named("jvmMain") {
        buildConfigField("API_KEY", stringPropertyOrNull("desktopApiKey"))
        buildConfigField("APP_ID", stringPropertyOrNull("desktopAppId"))
    }

    sourceSets.named("wasmJsMain") {
        buildConfigField("API_KEY", stringPropertyOrNull("browserApiKey"))
        buildConfigField("APP_ID", stringPropertyOrNull("browserAppId"))
    }

    className.set("BuildConfig")
    packageName.set(kotlin.android.namespace)
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

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.core.ui.components)
            implementation(projects.core.ui.resources)
            implementation(projects.core.ui.scaffold)
            implementation(projects.core.ui.snackbar)
            implementation(projects.features.eventCommon)
            implementation(projects.features.gallerySync)
            implementation(projects.features.pagerFactory)

            implementation(projects.httpCommon)
            implementation(projects.httpClient)
            implementation(projects.identityManager)
            implementation(projects.mapsRouting)
            implementation(projects.metroExtensions)
            implementation(projects.platformSupport)
            implementation(projects.remoteConfig)
            implementation(projects.sqlDriver)

            implementation(libs.androidx.paging.common)

            implementation(libs.circuit.runtime)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.overlay)
            implementation(libs.circuit.serialization)

            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsCore)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.ui)

            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.circuit.overlay)

            implementation(libs.google.accompanist.permissions)
            implementation(libs.google.android.location)
            implementation(libs.google.maps.android.compose)
            implementation(libs.google.maps.android.utils)

            implementation(dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.common)
            implementation(libs.google.firebase.appcheck.playintegrity)
        }

        androidJvmMain.dependencies {
            implementation(projects.composeMaterial)
        }

        jvmMain.dependencies {
            implementation(projects.keyNavigation)

            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)

            runtimeOnly(libs.kotlinx.coroutines.swing)
            runtimeOnly(libs.logback.classic)
        }

        jvmTest.dependencies {
            implementation(libs.androidx.paging.testing)
            implementation(libs.sqldelight.sqlite.driver)
        }

        jvmIntegrationTest.dependencies {
        }

        wasmJsMain.dependencies {
            implementation(projects.composeMaterial)
        }
    }
}

@OptIn(ExperimentalMetroGradleApi::class)
metro {
    enableCircuitCodegen = true
    enableSuspendProviders = true
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName = kotlin.android.namespace
            generateAsync = true

            dialect(libs.sqldelight.sqlite.dialect)
            dependency(project(":features:event-common"))
            dependency(project(":features:gallery-sync"))
            dependency(project(":identity-manager"))
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass.set(ConferenceAppConfig.MAIN_CLASS)
}
