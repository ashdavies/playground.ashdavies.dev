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

    implementation(ProjectDependencies.AndroidX.activity)
    implementation(ProjectDependencies.AndroidX.annotation)
    implementation(ProjectDependencies.AndroidX.appCompat)
    implementation(ProjectDependencies.AndroidX.cardView)
    implementation(ProjectDependencies.AndroidX.constraintLayout)
    implementation(ProjectDependencies.AndroidX.core)
    implementation(ProjectDependencies.AndroidX.fragment)
    implementation(ProjectDependencies.AndroidX.Lifecycle.liveData)
    implementation(ProjectDependencies.AndroidX.Lifecycle.viewModel)
    implementation(ProjectDependencies.AndroidX.paging)
    implementation(ProjectDependencies.AndroidX.Room.room)
    implementation(ProjectDependencies.AndroidX.Navigation.fragment)
    implementation(ProjectDependencies.AndroidX.Navigation.ui)
    implementation(ProjectDependencies.AndroidX.recyclerView)

    implementation(ProjectDependencies.Google.Firebase.common)
    implementation(ProjectDependencies.Google.Firebase.analytics)
    implementation(ProjectDependencies.Google.Firebase.firestore)
    implementation(ProjectDependencies.Google.materialDesign)

    implementation(ProjectDependencies.JetBrains.Kotlin.stdLib)
    implementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.android)
    implementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.core)
    implementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.play)

    kapt(ProjectDependencies.AndroidX.Room.compiler)

    testImplementation(ProjectDependencies.Google.truth)
    testImplementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.test)
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
