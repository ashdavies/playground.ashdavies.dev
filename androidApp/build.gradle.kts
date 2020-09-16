@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    //id("com.google.android.gms.oss-licenses-plugin")
    id("eu.appcom.gradle.android-versioning")

    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("kapt")
}

android {
    buildFeatures {
        compose = true
    }

    setCompileSdkVersion(29)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerVersion = BuildPlugins.KotlinGradlePlugin.version
        kotlinCompilerExtensionVersion = ProjectDependencies.AndroidX.Compose.version
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"

        setMinSdkVersion(21)
        setTargetSdkVersion(29)

        versionCode = versioning.getVersionCode()
        versionName = versioning.getVersionName()

        vectorDrawables.useSupportLibrary = true
    }

    sourceSets {
        getByName("main")
            .java
            .srcDirs("src/main/kotlin")

        getByName("test")
            .java
            .srcDirs("src/test/kotlin")
    }
}

kotlin {
    android {
        kotlinOptions {
            jvmTarget = "1.8"
            useIR = true

        }
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
    implementation(ProjectDependencies.AndroidX.Room.roomKtx)
    implementation(ProjectDependencies.AndroidX.Room.roomRuntime)
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
    implementation(ProjectDependencies.Square.Retrofit.converterSimplexml)
    implementation(ProjectDependencies.Square.Retrofit.retrofit)

    kapt(ProjectDependencies.AndroidX.Room.roomCompiler)

    testImplementation(ProjectDependencies.Google.truth)
    testImplementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesTest)
    testImplementation(ProjectDependencies.JUnit)
}

//apply(plugin = "com.google.gms.google-services")
