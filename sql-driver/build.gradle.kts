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

        wasmJsMain.dependencies {
            implementation(libs.sqldelight.web.driver)

            val sqlDelightVersion = libs.versions.cash.sqldelight.get()
            implementation(npm("@cashapp/sqldelight-sqljs-worker", sqlDelightVersion))

            val sqlJsVersion = libs.versions.sqlJs.get()
            implementation(npm("sql.js", sqlJsVersion))

            val webPackVersion = libs.versions.webPack.get()
            implementation(devNpm("copy-webpack-plugin", webPackVersion))
        }
    }
}
