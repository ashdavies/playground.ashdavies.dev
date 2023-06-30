plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.check.client"
}

kotlin {
    commonMain.dependencies {
        with(projects) {
            implementation(appCheck.appCheckCommon)
            implementation(firebaseCompose)
            implementation(cloudBackend.httpClient)
        }

        with(libs.ktor.client) {
            implementation(content.negotiation)
            implementation(core)
            implementation(json)
            implementation(logging)
            implementation(okhttp3)
        }

        implementation(libs.slf4j.simple)
    }

    androidDebug.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }

    androidMain.dependencies {
        with(libs.google.firebase) {
            implementation(analytics)
            implementation(common.ktx)
            implementation(appcheck.playintegrity)
        }
    }
}
