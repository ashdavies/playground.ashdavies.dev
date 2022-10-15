plugins {
    id("io.ashdavies.application")
}

kotlin {
    val androidMain by dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.bundles.androidx.activity)
    }
}
