plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.gallery"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeMaterial)
        implementation(projects.httpClient)
        implementation(projects.identityManager)
        implementation(projects.localStorage)
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
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.mock)
        implementation(libs.ktor.http)
        implementation(libs.ktor.io)
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
            dependency(projects.localStorage)
        }
    }
}
