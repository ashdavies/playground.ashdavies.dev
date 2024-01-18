plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.nsd"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.jetbrains.kotlinx.coroutines.core)
        //implementation(projects.platformSupport)
    }

    androidMain.dependencies {
        implementation(libs.androidx.annotation)
    }
}
