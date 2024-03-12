plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.sqldelight)
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
        implementation(projects.platformSupport)
        implementation(projects.sqlDriver)

        implementation(libs.androidx.credentials.auth)
        implementation(libs.google.android.identity)
        implementation(libs.kotlinx.coroutines.core)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
        }
    }
}
