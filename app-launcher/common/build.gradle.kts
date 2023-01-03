plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.common"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.materialIconsExtended)
        implementation(projects.dominionApp)
        implementation(projects.eventsApp)

        implementation(libs.bundles.arkivanov.decompose)
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
