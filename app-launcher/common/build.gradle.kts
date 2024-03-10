plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.common"
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
    }

    commonMain.dependencies {
        implementation(projects.afterParty)
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.circuitSupport)
        implementation(projects.composeMaterial)
        implementation(projects.dominionApp)
        implementation(projects.mapRoutes)
        implementation(projects.platformSupport)

        implementation(compose.components.resources)
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
