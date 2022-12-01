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

    androidMain.dependencies {
        implementation(libs.sqldelight.android.driver)
    }

    jvmMain.dependencies {
        implementation(libs.sqldelight.sqlite.driver)
    }
}
