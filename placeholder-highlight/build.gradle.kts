plugins {
    id("com.android.library")
    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    kotlin("multiplatform")
}

android {
    namespace = "io.ashdavies.placeholder"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.material3)
        implementation(compose.runtime)

        implementation(libs.androidx.annotation)
    }
}
