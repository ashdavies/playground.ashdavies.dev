plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.common"

    val main by sourceSets.getting {
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
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
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
    }

    jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
    }

    jvmTest.dependencies {
        implementation(kotlin("test"))

        implementation(libs.app.cash.turbine)
        implementation(libs.kotlinx.coroutines.test)
        implementation(libs.slack.circuit.test)
    }
}
