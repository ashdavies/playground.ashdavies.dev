plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.platform.support"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.squareup.okio)
    }
}
