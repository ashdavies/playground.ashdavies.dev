plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.paging.compose"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.androidx.paging.common)
    }
}
