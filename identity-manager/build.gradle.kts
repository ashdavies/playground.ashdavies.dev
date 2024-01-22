plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.properties")
    id("io.ashdavies.sql")
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val serverClientId by stringProperty { value ->
            buildConfigField("String", "SERVER_CLIENT_ID", "\"$value\"")
        }
    }

    namespace = "io.ashdavies.identity"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)
        implementation(projects.platformSupport)

        implementation(compose.runtime)

        implementation(libs.androidx.credentials.auth)
        implementation(libs.google.android.identity)
        implementation(libs.kotlinx.coroutines.core)
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
