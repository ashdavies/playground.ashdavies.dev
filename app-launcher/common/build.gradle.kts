plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.common"

    sourceSets["main"].apply {
        res.srcDirs(
            "src/androidMain/res",
            "src/commonMain/resources",
        )
    }
}

kotlin {
    commonMain.dependencies {
        api(projects.platformSupport)

        with(projects) {
            implementation(appCheck.appCheckClient)
            implementation(afterPartyApp)
            implementation(dominionApp)
            implementation(eventsApp)
            implementation(galleryApp)
            implementation(imageLoader)
        }

        implementation(libs.essenty.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    commonTest.dependencies {
        implementation(libs.slack.circuit.test)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
    }
}
