plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.routes"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.material3)
        implementation(compose.runtime)

        implementation(libs.essenty.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.google.maps.android.compose)
    }
}
