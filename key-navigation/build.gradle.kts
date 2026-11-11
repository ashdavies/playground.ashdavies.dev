plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.circuit.foundation)
    }
}
