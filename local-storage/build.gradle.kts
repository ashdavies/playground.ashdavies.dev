plugins {
    id("io.ashdavies.library")
    id("io.ashdavies.sql")
    id("android-manifest")
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
    }
}
