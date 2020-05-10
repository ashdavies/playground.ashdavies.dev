import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.github.ben-manes.versions")
    id("eu.appcom.gradle.android-versioning")

    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
}

android {
    buildFeatures {
        // compose = true
        dataBinding = true
    }

    setCompileSdkVersion(29)

    compileOptions {
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
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
        main.srcDirs = setOf("src/main/kotlin")
        test.srcDirs = setOf("src/test/kotlin")
    }
}

androidExtensions {
    features = setOf("parcelize")
}

dependencies {
    implementation(project(":mobile-ktx"))

    implementation("androidx.activity:activity-ktx:1.2.0-alpha04")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.appcompat:appcompat:1.2.0-beta01")
    implementation("androidx.cardview:cardview:1.0.0")
    // implementation("androidx.compose:compose-compiler:0.1.0-dev10")
    // implementation("androidx.compose:compose-runtime:0.1.0-dev10")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta5")
    implementation("androidx.core:core-ktx:1.3.0-rc01")
    implementation("androidx.fragment:fragment-ktx:1.3.0-alpha04")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha02")
    implementation("androidx.paging:paging-runtime:2.1.2")
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0-alpha06")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.0-alpha06")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha03")
    // implementation("androidx.ui:ui-animation:0.1.0-dev10")
    // implementation("androidx.ui:ui-foundation:0.1.0-dev10")
    // implementation("androidx.ui:ui-framework:0.1.0-dev10")
    // implementation("androidx.ui:ui-layout:0.1.0-dev10")
    // implementation("androidx.ui:ui-material:0.1.0-dev10")
    // implementation("androidx.ui:ui-tooling:0.1.0-dev10")

    implementation("com.dropbox.mobile.store:store4:4.0.0-alpha06")
    implementation("com.github.ashdavies:lifecycle-flow:0.7.2")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
    implementation("com.google.android.material:material:1.2.0-alpha06")
    implementation("com.google.firebase:firebase-common-ktx:19.3.0")
    implementation("com.google.firebase:firebase-analytics:17.4.1")
    implementation("com.google.firebase:firebase-firestore-ktx:21.4.3")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4-M1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.6")
    implementation("org.reduxkotlin:redux-kotlin-jvm:0.4.0")

    kapt("androidx.room:room-compiler:2.2.5")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")

    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0-M1")
}

tasks
    .withType<KotlinCompile>()
    .configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

apply(plugin = "com.google.gms.google-services")
