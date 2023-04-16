plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.localRemote)
        implementation(projects.playgroundApp)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.bundles.ktor.client)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))

        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.google.firebase)

        implementation(libs.google.accompanist.placeholderMaterial)
        implementation(libs.google.accompanist.swiperefresh)
        implementation(libs.google.firebase.auth.ktx)
    }
}
