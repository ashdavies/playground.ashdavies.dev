plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.local.storage"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.sqlDriver)
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
    }
}
