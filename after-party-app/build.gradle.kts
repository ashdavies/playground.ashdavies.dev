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

        implementation(libs.essenty.parcelable)
        implementation(libs.slack.circuit.foundation)
    }
}
