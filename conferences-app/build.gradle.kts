plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.sqldelight)
}

android {
    val androidTest by sourceSets.getting {
        res.srcDirs("src/androidMain/res")
    }

    val release by signingConfigs.creating {
        val debug by signingConfigs.getting
        initWith(debug)

        enableV3Signing = true
        enableV4Signing = true

        val keyStoreFile by stringPropertyOrNull {
            if (it != null) storeFile = rootProject.file(it)
        }

        val keyStorePassword by stringPropertyOrNull {
            if (it != null) storePassword = it
        }

        val releaseKeyAlias by stringPropertyOrNull {
            if (it != null) keyAlias = it
        }

        val releaseKeyPassword by stringPropertyOrNull {
            if (it != null) keyPassword = it
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = release

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    defaultConfig {
        versionName = "1.0"
        versionCode = 1
    }

    dependencies {
        coreLibraryDesugaring(libs.android.tools.desugarjdk)
    }

    namespace = "io.ashdavies.playground"
}

buildConfig {
    val androidApiKey by stringPropertyWithTag(::buildConfigField)
    val androidStrictMode by booleanPropertyWithTag(::buildConfigField)

    packageName.set(android.namespace)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.analytics)
            implementation(projects.appCheck.appCheckClient)
            implementation(projects.asgService)
            implementation(projects.composeMaterial)
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.identityManager)
            implementation(projects.pagingCompose)
            implementation(projects.placeholderHighlight)
            implementation(projects.platformScaffold)
            implementation(projects.platformSupport)
            implementation(projects.remoteConfig)
            implementation(projects.sqlCommon)
            implementation(projects.sqlCompose)
            implementation(projects.sqlDriver)

            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)

            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.paging.common)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.google.android.material)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.http)
            implementation(libs.ktor.io)
            implementation(libs.slack.circuit.foundation)
            implementation(libs.slack.circuit.overlay)
            implementation(libs.sqldelight.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.app.cash.turbine)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        val androidDebug by registering {
            dependencies.implementation(compose.uiTooling)
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.core.splashscreen)
                implementation(libs.google.android.material)
            }
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
            dependency(projects.identityManager)
        }
    }
}
