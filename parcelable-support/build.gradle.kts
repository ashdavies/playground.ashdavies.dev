plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.parcelable"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)
    }
}
