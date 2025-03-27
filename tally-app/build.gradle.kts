import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")

    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
    alias(libs.plugins.cash.paparazzi)
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

        val keyStoreFile by stringProperty { _, value ->
            if (value != null) storeFile = rootProject.file(value)
        }

        val keyStorePassword by stringProperty { _, value ->
            if (value != null) storePassword = value
        }

        val releaseKeyAlias by stringProperty { _, value ->
            if (value != null) keyAlias = value
        }

        val releaseKeyPassword by stringProperty { _, value ->
            if (value != null) keyPassword = value
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

    defaultConfig {
        val versionCode by stringProperty { _, value ->
            versionCode = value?.toInt() ?: 1
        }

        versionName = "1.0.0-$versionCode"
    }

    namespace = "io.ashdavies.playground"
}

buildConfig {
    val androidApiKey by stringProperty(::buildConfigField)
    val androidStrictMode by booleanProperty(::buildConfigField)
    val browserApiKey by stringProperty(::buildConfigField)

    packageName.set(android.namespace)
}

compose.desktop {
    application {
        mainClass = "io.ashdavies.tally.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg)

            packageName = "Tally"
            packageVersion = "1.0.0"
        }
    }
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

            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)

            implementation(libs.androidx.paging.common)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.window.size)
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
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.paging3.extensions)
            implementation(libs.sqldelight.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.app.cash.turbine)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.google.android.material)

            implementation(dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.crashlytics)
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
            implementation(projects.keyNavigation)
            implementation(compose.desktop.currentOs)

            runtimeOnly(libs.kotlinx.coroutines.swing)
            runtimeOnly(libs.slf4j.simple)
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
