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

        with(projects) {
            implementation(dominionApp)
            implementation(eventsApp)
            implementation(ratingsApp)
        }

        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
    }
}
