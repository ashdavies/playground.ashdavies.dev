plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.http"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.httpCommon)
            implementation(projects.platformSupport)

            implementation(libs.kotlinx.serialization.properties)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.squareup.okio)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}
