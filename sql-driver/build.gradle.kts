plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.sql"
}

kotlin {
    commonMain.dependencies {
        implementation(libs.sqldelight.runtime)
    }

    androidMain.dependencies {
        implementation(libs.sqldelight.android.driver)
    }

    jvmMain.dependencies {
        implementation(libs.sqldelight.sqlite.driver)
    }
}
