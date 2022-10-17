plugins {
    id("io.ashdavies.application")
}

kotlin {
    val commonMain by dependencies {
        implementation(project(":dominion-app"))
        implementation(project(":events-app"))

        implementation(libs.bundles.arkivanov.decompose)
        implementation(compose.materialIconsExtended)
    }

    val androidMain by dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.bundles.androidx.activity)
    }
}
