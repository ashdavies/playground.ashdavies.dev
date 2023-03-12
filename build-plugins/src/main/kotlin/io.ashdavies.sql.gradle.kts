plugins {
    id("com.squareup.sqldelight")
    kotlin("multiplatform")
}

kotlin {
    commonMain.dependencies {
        with(libs.sqldelight) {
            implementation(coroutines.extensions)
            implementation(runtime)
        }
    }
}
