plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.firebase.compose"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeLocals)
    }

    androidMain.dependencies {
        implementation(libs.bundles.google.firebase)
    }
}
