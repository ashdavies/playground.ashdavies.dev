plugins {
    id("io.ashdavies.default")
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
            implementation(httpClient)
            implementation(imageLoader)
            implementation(localStorage)
        }

        implementation(libs.paging.compose.common)
        implementation(libs.paging.compose.runtime)
        implementation(libs.slf4j.simple)
        implementation(libs.slack.circuit.foundation)
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
    databases {
        create("PlaygroundDatabase") {
            packageName.set("io.ashdavies.events")
            dependency(projects.localStorage)
        }
    }
}
