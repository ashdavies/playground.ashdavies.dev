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
    databases {
        create("PlaygroundDatabase") {
            packageName.set("io.ashdavies.playground")
        }
    }
}
