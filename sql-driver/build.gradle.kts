plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.sql.driver"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.platformSupport)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.sqldelight.async.extensions)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.runtime)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}
