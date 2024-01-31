plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.slack.circuit.foundation)
    }
}
