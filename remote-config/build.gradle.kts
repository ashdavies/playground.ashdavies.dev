plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.zac.metro)
}

kotlin {
    android.namespace = "dev.ashdavies.config"

    sourceSets {
        commonMain.dependencies {
            implementation(projects.httpClient)
            implementation(projects.sqlCommon)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.config)
        }
    }
}
