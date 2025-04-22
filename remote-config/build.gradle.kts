plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.config"
}

kotlin {
    sourceSets.androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(libs.google.firebase.config)
    }
}
