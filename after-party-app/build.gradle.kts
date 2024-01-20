plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.party"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeMaterial)
        implementation(projects.eventsApp)
        implementation(projects.galleryApp)
        implementation(projects.identityManager)

        implementation(compose.material)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.coil.compose)
        implementation(libs.slack.circuit.foundation)
    }

    androidDebug.dependencies {
        implementation(compose.uiTooling)
    }
}
