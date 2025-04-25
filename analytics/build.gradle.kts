plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.analytics"
}

kotlin {
    sourceSets.androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(libs.google.firebase.analytics)
    }
}
