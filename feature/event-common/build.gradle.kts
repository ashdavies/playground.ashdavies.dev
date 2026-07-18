plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.cash.sqldelight)
}

kotlin {
    android.namespace = "dev.ashdavies.playground.event.common"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.circuit.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
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
