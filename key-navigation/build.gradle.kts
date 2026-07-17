plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.circuit.foundation)
    }
}
