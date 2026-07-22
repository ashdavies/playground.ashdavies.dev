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
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.properties")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.zac.metro)
}

kotlin {
    android.namespace = ConferenceAppConfig.APPLICATION_NAME
}

buildConfig {
    buildConfigField("API_KEY", expect<String?>(null))
    buildConfigField("APP_ID", expect<String?>(null))

    buildConfigField("GOOGLE_CLOUD_PROJECT", stringPropertyOrNull("GOOGLE_CLOUD_PROJECT"))

    sourceSets.named("androidMain") {
        buildConfigField("API_KEY", stringPropertyOrNull("android.api.key"))
        buildConfigField("APP_ID", stringPropertyOrNull("android.app.id"))
    }

    sourceSets.named("jvmMain") {
        buildConfigField("API_KEY", stringPropertyOrNull("browser.api.key"))
        buildConfigField("APP_ID", stringPropertyOrNull("browser.app.id"))
    }

    sourceSets.named("wasmJsMain") {
        buildConfigField("API_KEY", stringPropertyOrNull("browser.api.key"))
        buildConfigField("APP_ID", stringPropertyOrNull("browser.app.id"))
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

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.asgService)
            implementation(projects.cloudCommon)
            implementation(projects.composeMaterial)
            implementation(projects.feature.eventCommon)
            implementation(projects.feature.eventDetail)
            implementation(projects.feature.eventGrid)
            implementation(projects.feature.eventList)
            implementation(projects.feature.gallerySync)
            implementation(projects.feature.pagerFactory)
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.identityManager)
            implementation(projects.mapsRouting)
            implementation(projects.platformSupport)
            implementation(projects.remoteConfig)
            implementation(projects.sqlCommon)
            implementation(projects.sqlDriver)
            implementation(projects.uiComponents)

            implementation(libs.androidx.annotation)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.window.core)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.overlay)
            implementation(libs.circuit.serialization)
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
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.paging3.extensions)
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
            implementation(libs.circuit.overlay)
            implementation(libs.google.accompanist.permissions)
            implementation(libs.google.android.location)
            implementation(libs.google.android.material)
            implementation(libs.google.maps.android.compose)
            implementation(libs.google.maps.android.utils)

            implementation(dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.appcheck.playintegrity)
            implementation(libs.google.firebase.crashlytics)

            implementation(libs.kotlinx.coroutines.play.services)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.keyNavigation)

            runtimeOnly(libs.kotlinx.coroutines.swing)
            runtimeOnly(libs.slf4j.simple)
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.androidx.paging.testing)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.sqldelight.sqlite.driver)
        }

        jvmIntegrationTest.dependencies {
            implementation(libs.app.cash.turbine)
            implementation(libs.kotlinx.coroutines.test)
        }

        wasmJsMain.dependencies {
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.overlay)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
        }
    }
}

metro {
    @OptIn(ExperimentalMetroGradleApi::class)
    enableCircuitCodegen = true
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName = kotlin.android.namespace
            generateAsync = true

            dialect(libs.sqldelight.sqlite.dialect)
            dependency(project(":feature:event-common"))
            dependency(project(":feature:gallery-sync"))
            dependency(project(":identity-manager"))
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass.set(ConferenceAppConfig.MAIN_CLASS)
}
