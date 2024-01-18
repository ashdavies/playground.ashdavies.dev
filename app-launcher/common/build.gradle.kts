plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.common"

    val main by sourceSets.getting {
        res.srcDirs("src/commonMain/resources")
    }
}

kotlin {
    commonMain.dependencies {
        implementation(projects.afterPartyApp)
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.composeMaterial)
        implementation(projects.dominionApp)
        implementation(projects.eventsApp)
        implementation(projects.galleryApp)
        implementation(projects.platformSupport)

        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.coil.compose)
        implementation(libs.coil.network)
        implementation(libs.essenty.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
    }
}
