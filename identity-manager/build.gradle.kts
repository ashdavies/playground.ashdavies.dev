plugins {
    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.sqldelight)

    id("dev.ashdavies.android.library")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.properties")
    id("dev.ashdavies.wasm")
}

kotlin {
    android {
        namespace = "dev.ashdavies.identity"
    }
}

buildConfig {
    val serverClientId by stringProperty(::buildConfigField)

    className.set("BuildConfig")
    packageName.set(kotlin.android.namespace)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.kotlinDelegates)
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
