plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.gallery"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeLocals)
        implementation(projects.httpClient)
        implementation(projects.imageLoader)
        implementation(projects.localStorage)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
    }
}


sqldelight {
    database("PlaygroundDatabase") {
        dependency(projects.localStorage.dependencyProject)
        packageName = "io.ashdavies.gallery"
    }
}
