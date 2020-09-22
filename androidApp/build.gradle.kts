import BuildPlugins.KotlinGradlePlugin
import ProjectDependencies.AndroidX.Compose.version as ComposeVersion

plugins {
    id("com.android.application")
    id("com.squareup.sqldelight")

    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerVersion = KotlinGradlePlugin.version
        kotlinCompilerExtensionVersion = ComposeVersion
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"

        setMinSdkVersion(21)
        setTargetSdkVersion(29)

        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true
    }

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )

        jvmTarget = "1.8"
        useIR = true
    }

    setCompileSdkVersion(29)

    sourceSets.configureEach {
        java.srcDirs("src/$name/kotlin")
    }
}

dependencies {
    implementation(ProjectDependencies.AndroidX.activityKtx)
    implementation(ProjectDependencies.AndroidX.annotation)
    implementation(ProjectDependencies.AndroidX.Compose.foundation)
    implementation(ProjectDependencies.AndroidX.Compose.material)
    implementation(ProjectDependencies.AndroidX.Compose.navigation)
    implementation(ProjectDependencies.AndroidX.Compose.runtime)
    implementation(ProjectDependencies.AndroidX.Compose.ui)
    implementation(ProjectDependencies.AndroidX.coreKtx)
    implementation(ProjectDependencies.AndroidX.fragmentKtx)
    implementation(ProjectDependencies.AndroidX.Lifecycle.lifecycleLivedataKtx)
    implementation(ProjectDependencies.AndroidX.Lifecycle.lifecycleViewmodelKtx)
    implementation(ProjectDependencies.AndroidX.pagingRuntime)
    implementation(ProjectDependencies.AndroidX.Navigation.navigationFragmentKtx)
    implementation(ProjectDependencies.AndroidX.Navigation.navigationUiKtx)
    implementation(ProjectDependencies.AndroidX.Ui.uiTooling)
    implementation(ProjectDependencies.Dropbox.store4)
    implementation(ProjectDependencies.Google.Firebase.firebaseCommonKtx)
    implementation(ProjectDependencies.Google.Firebase.firebaseAnalytics)
    implementation(ProjectDependencies.Google.Android.material)
    implementation(ProjectDependencies.JakeWharton.retrofit2KotlinxSerializationConverter)
    implementation(ProjectDependencies.JetBrains.Kotlin.kotlinSerialization)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesAndroid)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(ProjectDependencies.Square.okhttp)
    implementation(ProjectDependencies.Square.SqlDelight.androidDriver)
    implementation(ProjectDependencies.Square.SqlDelight.coroutinesExtensionsJvm)
    implementation(ProjectDependencies.Square.SqlDelight.runtime)
    implementation(ProjectDependencies.Square.Retrofit.converterSimplexml)
    implementation(ProjectDependencies.Square.Retrofit.retrofit)

    testImplementation(ProjectDependencies.Google.truth)
    testImplementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesTest)
    testImplementation(ProjectDependencies.JUnit)
    testImplementation(ProjectDependencies.Square.SqlDelight.sqliteDriver)
}
