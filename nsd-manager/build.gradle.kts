plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.nsd"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.kotlinx.coroutines.core)
    }

    androidMain.dependencies {
        implementation(libs.androidx.annotation)
    }
}
