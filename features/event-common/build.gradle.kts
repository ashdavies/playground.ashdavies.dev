plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.cash.sqldelight)
}

kotlin {
    android.namespace = "dev.ashdavies.playground.event.common"

    sourceSets {
        commonMain.dependencies {
            api(projects.httpCommon)

            implementation(libs.circuit.serialization)
            implementation(libs.metro.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            dialect(libs.sqldelight.sqlite.dialect)
            packageName = kotlin.android.namespace
            generateAsync = true
        }
    }
}
