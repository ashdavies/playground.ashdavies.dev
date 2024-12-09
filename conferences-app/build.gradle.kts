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
    alias(libs.plugins.cash.sqldelight)
    alias(libs.plugins.compose.screenshot)
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

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    defaultConfig {
        val versionCode by stringProperty { _, value ->
            versionCode = value?.toInt() ?: 1
        }

        versionName = "1.0.0"
    }

    dependencies {
        coreLibraryDesugaring(libs.android.tools.desugarjdk)

        screenshotTestImplementation(compose.material3)
        screenshotTestImplementation(compose.uiTooling)

        screenshotTestImplementation(libs.androidx.paging.common)
        screenshotTestImplementation(libs.compose.window.size)
        screenshotTestImplementation(libs.slack.circuit.runtime)
        screenshotTestImplementation(libs.kotlinx.collections.immutable)

        screenshotTestImplementation(projects.identityManager)
        screenshotTestImplementation(projects.pagingCompose)
    }

    experimentalProperties["android.experimental.enableScreenshotTest"] = true

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
        mainClass = "io.ashdavies.party.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg)

            packageName = "ConferencesApp"
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

            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.paging.common)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.window.size)
            implementation(libs.gitlive.firebase.app)
            implementation(libs.gitlive.firebase.config)
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

screenshotTests {
    imageDifferenceThreshold = 0.012f // 1.2 %
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set(android.namespace)
            dependency(projects.identityManager)
        }
    }
}
