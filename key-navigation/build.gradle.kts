plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.slack.circuit.foundation)
    }
}
