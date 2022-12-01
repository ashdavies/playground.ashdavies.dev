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
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
