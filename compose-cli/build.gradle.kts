plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
}

kotlin {
    commonMain.dependencies {
        api(libs.jetbrains.kotlinx.cli)
    }
}
