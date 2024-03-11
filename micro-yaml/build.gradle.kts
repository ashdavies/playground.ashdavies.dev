plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    commonMain.dependencies {
        implementation(libs.kotlinx.serialization.core)
    }
}
