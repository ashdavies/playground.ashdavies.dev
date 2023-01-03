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
        implementation(platform(libs.google.firebase.bom))
        implementation(libs.bundles.google.firebase)
    }
}
