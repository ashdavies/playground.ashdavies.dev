plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.check.client"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.appCheck.appCheckCommon)
            implementation(projects.httpClient)

            implementation(libs.ktor.client.core)
        }

        androidMain.dependencies {
            implementation(dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.appcheck.playintegrity)
        }
    }
}
