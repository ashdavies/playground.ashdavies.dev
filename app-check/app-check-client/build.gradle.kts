plugins {
    id("io.ashdavies.library")
    id("android-manifest")
}

kotlin {
    val commonMain by dependencies {
        implementation(project(":firebase-compose"))
        implementation(project(":local-remote"))

        implementation(libs.bundles.ktor.client)
    }

    val androidDebug by dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }

    val androidMain by dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.bundles.google.firebase)
        implementation(libs.google.firebase.appcheck.playintegrity)
    }
}
