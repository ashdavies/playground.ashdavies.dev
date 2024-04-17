plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.nsd"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.annotation)
        }
    }
}
