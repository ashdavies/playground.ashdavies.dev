plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.common"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.foundation)
        implementation(compose.materialIconsExtended)
        implementation(compose.ui)

        implementation(projects.dominionApp)
        implementation(projects.eventsApp)

        implementation(libs.bundles.arkivanov.decompose)
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
