plugins {
    id("dev.ashdavies.default")
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
