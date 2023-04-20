plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.common"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.materialIconsExtended)
        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
        implementation(projects.dominionApp)
        implementation(projects.eventsApp)
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
