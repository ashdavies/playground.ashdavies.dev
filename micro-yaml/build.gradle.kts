plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
}

kotlin {
    commonMain.dependencies {
        implementation(libs.kotlinx.serialization.core)
    }
}
