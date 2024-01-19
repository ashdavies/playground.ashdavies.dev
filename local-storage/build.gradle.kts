plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.local.storage"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(projects.platformSupport)
        implementation(projects.sqlDriver)

        implementation(libs.jetbrains.kotlinx.serialization.core)
        implementation(libs.sqldelight.coroutines.extensions)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set("io.ashdavies.playground")
        }
    }
}
