@file:Suppress("UnstableApiUsage")

import eu.appcom.gradle.VersioningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("com.google.android.gms.oss-licenses-plugin")

    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")

    id("eu.appcom.gradle.android-versioning")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    buildFeatures {
        viewBinding = true
    }

    setCompileSdkVersion(29)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"

        setMinSdkVersion(21)
        setTargetSdkVersion(29)

        val versioning: VersioningExtension = extensions
            .getByName("versioning")
            .let { it as VersioningExtension }

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

androidExtensions {
    features = setOf("parcelize")
}

configurations {
    create("ktlint")
}

dependencies {
    implementation(project(":mobile-ktx"))

    implementation(ProjectDependencies.AndroidX.Activity)
    implementation(ProjectDependencies.AndroidX.Annotation)
    implementation(ProjectDependencies.AndroidX.AppCompat)
    implementation(ProjectDependencies.AndroidX.CardView)
    implementation(ProjectDependencies.AndroidX.ConstraintLayout)
    implementation(ProjectDependencies.AndroidX.Core)
    implementation(ProjectDependencies.AndroidX.Fragment)
    implementation(ProjectDependencies.AndroidX.Lifecycle.LiveData)
    implementation(ProjectDependencies.AndroidX.Lifecycle.ViewModel)
    implementation(ProjectDependencies.AndroidX.Paging)
    implementation(ProjectDependencies.AndroidX.Room.Room)
    implementation(ProjectDependencies.AndroidX.Navigation.Fragment)
    implementation(ProjectDependencies.AndroidX.Navigation.Ui)
    implementation(ProjectDependencies.AndroidX.RecyclerView)

    implementation(ProjectDependencies.Google.Firebase.Common)
    implementation(ProjectDependencies.Google.Firebase.Analytics)
    implementation(ProjectDependencies.Google.Firebase.Firestore)
    implementation(ProjectDependencies.Google.MaterialDesign)

    implementation(ProjectDependencies.JetBrains.Kotlin.StdLib)
    implementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.Android)
    implementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.Core)
    implementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.Play)

    kapt(ProjectDependencies.AndroidX.Room.Compiler)

    testImplementation(ProjectDependencies.Google.Truth)
    testImplementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.Test)
    testImplementation(ProjectDependencies.JUnit)

    // ktlint(ProjectDependencies.Pinterest.KtLint)
}

tasks
    .withType<KotlinCompile>()
    .configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

/*task("ktlint") {
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.ktlint
    args "src/**/*.kt"
}

(type: JavaExec, group: "verification") {
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.ktlint
    args "src/**/*.kt"
}

check.dependsOn ktlint
    task ktlintFormat(type: JavaExec, group: "formatting") {
    description = "Fix Kotlin code style deviations."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.ktlint
    args "-F", "src/**/*.kt"
}*/

apply(plugin = "com.google.gms.google-services")
