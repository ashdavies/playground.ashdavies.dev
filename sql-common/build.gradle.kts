plugins {
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.sql.common"
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
