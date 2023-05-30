plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.events"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.material)

        implementation(projects.appCheck.appCheckClient)
        implementation(projects.composeLocals)

        implementation(projects.authOauth)
        implementation(projects.cloudBackend.httpClient)
        implementation(projects.localStorage)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.paging.compose)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.sqldelight.coroutines.extensions)
    }

    androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(projects.firebaseCompose)

        with(libs.bundles) {
            implementation(androidx.activity)
            implementation(google.firebase)
            implementation(google.maps)
        }

        with(libs.google) {
            implementation(accompanist.flowlayout)
            implementation(accompanist.placeholderMaterial)
            implementation(firebase.appcheck.playintegrity)
            implementation(firebase.appcheck)
            implementation(firebase.auth.ktx)
        }
    }

    androidDebug.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        dependency(projects.localStorage.dependencyProject)
        packageName = "io.ashdavies.events"
    }
}
