plugins {
    id("dev.ashdavies.default")
}

android {
    namespace = "io.ashdavies.sql.driver"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.platformSupport)
            implementation(projects.sqlCommon)

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

        wasmJsMain.dependencies {
            implementation(libs.sqldelight.web.driver)

            implementation(npm("@cashapp/sqldelight-sqljs-worker", libs.versions.cash.sqldelight.get()))
            implementation(npm("sql.js", libs.versions.sqlJs.get()))

            implementation(devNpm("copy-webpack-plugin", libs.versions.webPack.get()))
        }
    }
}
