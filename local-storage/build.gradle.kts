plugins {
    id("io.ashdavies.default")
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
