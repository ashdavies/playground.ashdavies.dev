plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.check.client"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckCommon)
        implementation(projects.firebaseCompose)
        implementation(projects.localRemote)

        implementation(libs.bundles.ktor.client)
    }

    androidDebug.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }

    androidMain.dependencies {
        implementation(libs.bundles.google.firebase)
        implementation(libs.google.firebase.appcheck.playintegrity)
    }
}
