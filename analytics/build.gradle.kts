plugins {
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.analytics"
}

kotlin {
    sourceSets.androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(libs.google.firebase.analytics)
    }
}
