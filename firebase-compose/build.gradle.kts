plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.firebase.compose"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)
        implementation(libs.gitlive.firebase.app)
    }
}
