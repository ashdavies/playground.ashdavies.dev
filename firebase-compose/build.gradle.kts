plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.firebase.compose"
}

kotlin {
    androidMain.dependencies {
        with(libs.google.firebase) {
            implementation(dependencies.platform(bom))
            implementation(analytics)
            implementation(common.ktx)
        }
    }
}
