plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.sql.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.runtime)
        }
    }
}
