import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.sqldelight)

    id("dev.ashdavies.android.library")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.wasm")
}

buildConfig {
    buildConfigField("SERVER_CLIENT_ID", getLocalProperty("server.client.id"))

    className.set("BuildConfig")
    packageName.set(kotlin.android.namespace)
}

kotlin {
    android.namespace = "dev.ashdavies.identity"

    sourceSets {
        commonMain.dependencies {
            implementation(projects.platformSupport)
            implementation(projects.sqlCommon)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.sqldelight.coroutines.extensions)
        }

        androidMain.dependencies {
            implementation(libs.androidx.credentials.auth)
            implementation(libs.google.android.identity)
        }
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName = kotlin.android.namespace
            generateAsync = true
        }
    }
}
