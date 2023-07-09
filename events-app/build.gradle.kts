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

        with(projects) {
            implementation(appCheck.appCheckClient)
            implementation(composeLocals)
            implementation(httpClient)
            implementation(imageLoader)
            implementation(localStorage)
        }

        implementation(libs.arkivanov.parcelable)

        with(libs.ktor.client) {
            implementation(content.negotiation)
            implementation(core)
            implementation(json)
            implementation(logging)
            implementation(okhttp3)
        }

        implementation(libs.paging.compose.common)
        implementation(libs.paging.compose.runtime)
        implementation(libs.slf4j.simple)
        implementation(libs.slack.circuit.foundation)
        implementation(libs.sqldelight.coroutines.extensions)
    }

    androidMain.dependencies {
        implementation(projects.firebaseCompose)

        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)

        with(libs.google) {
            implementation(android.maps)
            implementation(accompanist.flowlayout)
            implementation(accompanist.placeholderMaterial)

            with(firebase) {
                implementation(dependencies.platform(bom))
                implementation(analytics)
                implementation(appcheck.playintegrity)
                implementation(appcheck)
                implementation(auth.ktx)
                implementation(common.ktx)
            }

            implementation(maps.android.compose)
            implementation(maps.android.compose.widgets)
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
