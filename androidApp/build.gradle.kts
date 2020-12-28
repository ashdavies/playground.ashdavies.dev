import BuildPlugins.KotlinGradlePlugin
import ProjectDependencies.AndroidX.Compose.version as ComposeVersion

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
        kotlinCompilerVersion = KotlinGradlePlugin.version
        kotlinCompilerExtensionVersion = ComposeVersion
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

    implementation(ProjectDependencies.AndroidX.activityKtx)
    implementation(ProjectDependencies.AndroidX.annotation)
    implementation(ProjectDependencies.AndroidX.Compose.foundation)
    implementation(ProjectDependencies.AndroidX.Compose.material)
    implementation(ProjectDependencies.AndroidX.Compose.runtime)
    implementation(ProjectDependencies.AndroidX.Compose.ui)
    implementation(ProjectDependencies.AndroidX.Compose.uiTooling)
    implementation(ProjectDependencies.AndroidX.coreKtx)
    implementation(ProjectDependencies.AndroidX.fragmentKtx)
    implementation(ProjectDependencies.AndroidX.Lifecycle.lifecycleLivedataKtx)
    implementation(ProjectDependencies.AndroidX.Lifecycle.lifecycleViewmodelKtx)
    implementation(ProjectDependencies.AndroidX.pagingRuntime)
    implementation(ProjectDependencies.AndroidX.Navigation.navigationCompose)
    implementation(ProjectDependencies.AndroidX.Navigation.navigationRuntimeKtx)
    implementation(ProjectDependencies.AndroidX.Navigation.navigationUiKtx)
    implementation(ProjectDependencies.Dropbox.store4)
    implementation(ProjectDependencies.Google.Firebase.firebaseCommonKtx)
    implementation(ProjectDependencies.Google.Firebase.firebaseAnalytics)
    implementation(ProjectDependencies.Google.Android.material)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesAndroid)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(ProjectDependencies.JetBrains.KotlinX.kotlinxDatetime)
    implementation(ProjectDependencies.Ktor.ktorClientCore)
    implementation(ProjectDependencies.Square.SqlDelight.androidDriver)
    implementation(ProjectDependencies.Square.SqlDelight.coroutinesExtensions)
    implementation(ProjectDependencies.Square.SqlDelight.runtime)

    testImplementation(ProjectDependencies.JetBrains.Kotlin.kotlinTest)
    testImplementation(ProjectDependencies.JetBrains.Kotlin.kotlinTestJunit)
    testImplementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesTest)
    testImplementation(ProjectDependencies.Square.SqlDelight.sqliteDriver)
}
