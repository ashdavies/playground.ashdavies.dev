plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.platform.scaffold"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.runtime)

        implementation(projects.platformSupport)
    }

    sourceSets.androidMain.dependencies {
        implementation(compose.ui)
    }
}
