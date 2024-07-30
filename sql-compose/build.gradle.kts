plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.sql.compose"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.platformScaffold)
            implementation(projects.platformSupport)
            implementation(projects.sqlDriver)

            implementation(libs.sqldelight.runtime)
        }
    }
}
