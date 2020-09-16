@file:Suppress("UnstableApiUsage")

import BuildPlugins.KotlinGradlePlugin
import ProjectDependencies.AndroidX.Compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    //id("com.google.android.gms.oss-licenses-plugin")
    id("eu.appcom.gradle.android-versioning")

    //kotlin("multiplatform")
    //kotlin("plugin.serialization")
    //kotlin("kapt")
}

configurations {
    create("composeCompiler") {
        isCanBeConsumed = false
    }
}

android {
    afterEvaluate {
        // Compose-MPP workaround: https://github.com/avdim/compose_mpp_workaround
        val composeCompilerJar = configurations["composeCompiler"]
            .resolve()
            .single()

        tasks.withType<KotlinCompile> {
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xplugin=$composeCompilerJar",
                "-Xuse-ir"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerVersion = KotlinGradlePlugin.version
        kotlinCompilerExtensionVersion = Compose.version
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"

        setMinSdkVersion(21)
        setTargetSdkVersion(29)

        versionCode = versioning.getVersionCode()
        versionName = versioning.getVersionName()

        vectorDrawables.useSupportLibrary = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    setCompileSdkVersion(29)

    sourceSets.configureEach {
        java.srcDirs("src/$name/kotlin")
    }
}

kotlin {
    android()
}

dependencies {
    "composeCompiler"(Compose.composeCompiler)

    implementation(ProjectDependencies.AndroidX.activityKtx)
    implementation(ProjectDependencies.AndroidX.annotation)
    implementation(Compose.foundation)
    implementation(Compose.material)
    implementation(Compose.navigation)
    implementation(Compose.runtime)
    implementation(Compose.ui)
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
    //implementation(ProjectDependencies.JetBrains.Kotlin.kotlinSerialization)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesAndroid)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(ProjectDependencies.Square.okhttp)
    implementation(ProjectDependencies.Square.Retrofit.converterSimplexml)
    implementation(ProjectDependencies.Square.Retrofit.retrofit)

    //"kapt"(ProjectDependencies.AndroidX.Room.roomCompiler)

    testImplementation(ProjectDependencies.Google.truth)
    testImplementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesTest)
    testImplementation(ProjectDependencies.JUnit)
}

//apply(plugin = "com.google.gms.google-services")
