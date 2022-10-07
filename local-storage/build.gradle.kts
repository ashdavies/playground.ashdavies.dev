plugins {
    id("io.ashdavies.library")
    id("io.ashdavies.sql")
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
    }
}
