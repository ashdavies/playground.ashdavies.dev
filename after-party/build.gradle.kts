plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.party"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.circuitSupport)
        implementation(projects.composeMaterial)
        implementation(projects.httpClient)
        implementation(projects.httpCommon)
        implementation(projects.identityManager)
        implementation(projects.platformScaffold)
        implementation(projects.platformSupport)
        implementation(projects.sqlDriver)

        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.coil.compose)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.mock)
        implementation(libs.ktor.http)
        implementation(libs.ktor.io)
        implementation(libs.paging.compose.common)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.sqldelight.runtime)
    }

    commonTest.dependencies {
        implementation(kotlin("test"))

        implementation(libs.app.cash.turbine)
        implementation(libs.kotlinx.coroutines.test)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.google.accompanist.flowlayout)
        implementation(libs.google.accompanist.placeholderMaterial)
    }

    androidDebug.dependencies {
        implementation(compose.uiTooling)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
            dependency(projects.identityManager)
        }
    }
}
