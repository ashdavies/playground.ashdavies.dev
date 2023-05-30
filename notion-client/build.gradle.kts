plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.notion"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeLocals)
        implementation(projects.localStorage)
        implementation(projects.sqlDriver)

        api(libs.jraf.klibnotion)
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        dependency(projects.localStorage.dependencyProject)
        packageName = "io.ashdavies.notion"
    }
}
