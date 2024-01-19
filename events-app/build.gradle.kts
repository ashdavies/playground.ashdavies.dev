plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.events"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.httpClient)
        implementation(projects.httpCommon)
        implementation(projects.localStorage)

        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(compose.runtime)

        implementation(libs.coil.compose)
        implementation(libs.essenty.parcelable)
        implementation(libs.jetbrains.kotlinx.datetime)
        implementation(libs.jetbrains.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
        implementation(libs.paging.compose.common)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
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
            packageName.set("io.ashdavies.events")
            dependency(projects.localStorage)
        }
    }
}
