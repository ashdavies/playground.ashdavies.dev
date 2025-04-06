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
    val serverClientId by stringProperty(::buildConfigField)

    className.set("BuildConfig")
    packageName.set(android.namespace)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.kotlinDelegates)
            implementation(projects.platformSupport)
            implementation(projects.sqlCommon)

            implementation(libs.kotlinx.coroutines.core)
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
            packageName.set(android.namespace)
        }
    }
}
