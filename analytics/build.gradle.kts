plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.analytics"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.gitlive.firebase.analytics)
    }
}
