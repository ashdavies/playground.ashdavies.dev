plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.dominion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.localRemote)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.ktor.client)
    }

    androidMain.dependencies {
        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.google.firebase)

        implementation(libs.google.accompanist.placeholderMaterial)
        implementation(libs.google.accompanist.swiperefresh)
    }
}
