plugins {
    id("io.ashdavies.application")
}

android {
    namespace = "io.ashdavies.playground"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(compose.materialIconsExtended)
        implementation(libs.bundles.arkivanov.decompose)

        implementation(projects.dominionApp)
        implementation(projects.eventsApp)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.bundles.androidx.activity)
    }
}
