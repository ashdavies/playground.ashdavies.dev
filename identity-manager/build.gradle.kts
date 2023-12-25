plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "SERVER_CLIENT_ID", "\"${System.getenv("SERVER_CLIENT_ID")}\"")
    }

    namespace = "io.ashdavies.identity"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)
        implementation(projects.localStorage)
        implementation(projects.sqlDriver)
    }

    androidMain.dependencies {
        implementation(libs.androidx.credentials.auth)
        implementation(libs.google.android.identity)
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
