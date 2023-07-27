plugins {
    id("app.cash.sqldelight")
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
