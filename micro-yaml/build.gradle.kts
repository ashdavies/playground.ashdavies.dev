plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    commonMain.dependencies {
        implementation(libs.jetbrains.kotlinx.serialization.core)
    }
}
