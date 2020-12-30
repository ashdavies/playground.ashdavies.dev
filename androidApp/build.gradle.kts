import BuildPlugins.KotlinGradlePlugin
import ProjectDependencies.AndroidX
import ProjectDependencies.Dropbox
import ProjectDependencies.Google
import ProjectDependencies.JetBrains
import ProjectDependencies.Ktor
import ProjectDependencies.Square

plugins {
    id("com.android.application")
    id("com.squareup.sqldelight")

    kotlin("android")
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
        kotlinCompilerExtensionVersion = AndroidX.Compose.version
        kotlinCompilerVersion = KotlinGradlePlugin.version
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"

        setMinSdkVersion(23)
        setTargetSdkVersion(30)

        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true
    }

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-Xallow-result-return-type",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview"
        )

        jvmTarget = "1.8"
        useIR = true
    }

    setCompileSdkVersion(29)

    sourceSets.configureEach {
        java.srcDirs("src/$name/kotlin")
    }

    /*packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }*/
}

dependencies {
    implementation(project(":shared"))

    implementation(AndroidX.activityKtx)
    implementation(AndroidX.annotation)
    implementation(AndroidX.Compose.foundation)
    implementation(AndroidX.Compose.material)
    implementation(AndroidX.Compose.runtime)
    implementation(AndroidX.Compose.ui)
    implementation(AndroidX.Compose.uiTooling)
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.fragmentKtx)
    implementation(AndroidX.Lifecycle.lifecycleLivedataKtx)
    implementation(AndroidX.Lifecycle.lifecycleViewmodelKtx)
    implementation(AndroidX.pagingRuntime)
    implementation(AndroidX.Navigation.navigationCompose)
    implementation(AndroidX.Navigation.navigationRuntimeKtx)
    implementation(AndroidX.Navigation.navigationUiKtx)
    implementation(Dropbox.store4)
    implementation(Google.Firebase.firebaseCommonKtx)
    implementation(Google.Firebase.firebaseAnalytics)
    implementation(Google.Android.material)
    implementation(JetBrains.KotlinX.kotlinxCoroutinesAndroid)
    implementation(JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(Ktor.ktorClientCore)
    implementation(Square.SqlDelight.androidDriver)
    implementation(Square.SqlDelight.coroutinesExtensions)
    implementation(Square.SqlDelight.runtime)

    testImplementation(JetBrains.Kotlin.kotlinTest)
    testImplementation(JetBrains.Kotlin.kotlinTestJunit)
    testImplementation(JetBrains.KotlinX.kotlinxCoroutinesTest)
    testImplementation(Square.SqlDelight.sqliteDriver)
}
