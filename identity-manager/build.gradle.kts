plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.properties")
    id("io.ashdavies.sql")

    alias(libs.plugins.build.config)
}

android {
    namespace = "io.ashdavies.identity"
}

buildConfig {
    val serverClientId by stringProperty { value ->
        buildConfigField("SERVER_CLIENT_ID", value)
    }

    packageName.set(android.namespace)
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)
        implementation(projects.platformSupport)

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
