plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.identity"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)
        implementation(projects.localStorage)
        implementation(projects.sqlDriver)
    }
}


sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
            dependency(projects.localStorage)
        }
    }
}
