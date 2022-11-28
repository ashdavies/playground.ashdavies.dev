plugins {
    id("com.android.library")
    id("io.ashdavies.library")
}

android {
    namespace = "io.ashdavies.check.client"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.firebaseCompose)
        implementation(projects.localRemote)

        implementation(libs.bundles.ktor.client)
    }

    val androidDebug by sourceSets.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.bundles.google.firebase)
        implementation(libs.google.firebase.appcheck.playintegrity)
    }
}
