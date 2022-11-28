plugins {
    id("com.android.library")
    id("io.ashdavies.kotlin")
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
