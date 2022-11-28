plugins {
    id("com.android.library")
    id("io.ashdavies.library")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.localRemote)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.ktor.client)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.google.firebase)

        implementation(libs.google.accompanist.placeholderMaterial)
        implementation(libs.google.accompanist.swiperefresh)
    }
}
