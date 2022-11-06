plugins {
    id("io.ashdavies.application")
}

android {
    namespace = "io.ashdavies.playground"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(libs.bundles.arkivanov.decompose)
        implementation(compose.materialIconsExtended)
        implementation(projects.dominionApp)
        implementation(projects.eventsApp)
        implementation(projects.firebaseCompose)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.google.firebase)
    }
}
