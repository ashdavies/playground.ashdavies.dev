plugins {
    id("io.ashdavies.library")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.local.storage"
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
    }
}
