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
            implementation(httpClient)
        }

        implementation(libs.ktor.client.core)
        implementation(libs.slf4j.simple)
    }

    androidDebug.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }

    androidMain.dependencies {
        implementation(libs.gitlive.firebase.app)
        implementation(libs.google.firebase.appcheck.playintegrity)
    }
}
