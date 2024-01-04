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
        implementation(compose.materialIconsExtended)

        with(projects) {
            implementation(composeMaterial)
            implementation(httpClient)
            implementation(identityManager)
            implementation(localStorage)
            implementation(platformSupport)
            implementation(sqlDriver)
        }

        implementation(libs.coil.compose)
        implementation(libs.essenty.parcelable)
        implementation(libs.ktor.client.mock)
        implementation(libs.slack.circuit.foundation)
    }

    commonTest.dependencies {
        with(libs.ktor) {
            implementation(client.content.negotiation)
            implementation(serialization.json)
            implementation(client.mock)
        }
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
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
