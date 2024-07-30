plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.config"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.gitlive.firebase.config)
    }
}
