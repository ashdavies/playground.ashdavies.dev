plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.kotlinx.cli)
    }
}
