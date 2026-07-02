plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android.namespace = "dev.ashdavies.sql"

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

            val sqldelightVersion = libs.versions.cash.sqldelight.get()
            val sqldelightNpmVersion = if (sqldelightVersion.endsWith("-SNAPSHOT")) "2.3.2" else sqldelightVersion
            println("=== sqlDelightNpmVersion = $sqldelightNpmVersion")

            implementation(npm("@cashapp/sqldelight-sqljs-worker", sqldelightNpmVersion))
            implementation(npm("sql.js", libs.versions.sqlJs.get()))

            implementation(devNpm("copy-webpack-plugin", libs.versions.webPack.get()))
        }
    }
}
