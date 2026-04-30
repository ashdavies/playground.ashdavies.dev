plugins {
    alias(libs.plugins.android.library)

    id("dev.ashdavies.android")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.parcelable")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.cash.sqldelight)
}

android {
    namespace = "dev.ashdavies.event.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.slack.circuit.runtime)
        }
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            dialect(libs.sqldelight.sqlite.dialect)
            packageName = android.namespace
            generateAsync = true
        }
    }
}
