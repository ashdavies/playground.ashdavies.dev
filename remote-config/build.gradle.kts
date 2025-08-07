plugins {
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.config"
}

kotlin {
    sourceSets.androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(libs.google.firebase.config)
    }
}
