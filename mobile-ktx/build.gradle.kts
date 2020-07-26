@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")

    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    setCompileSdkVersion(29)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        setMinSdkVersion(21)
        setTargetSdkVersion(29)
    }

    sourceSets {
        getByName("main")
            .java
            .srcDir("src/main/kotlin")

        getByName("test")
            .java
            .srcDirs("src/test/kotlin")
    }
}

dependencies {
    implementation("androidx.databinding:viewbinding:4.2.0-alpha05")
    implementation("androidx.annotation:annotation:1.2.0-alpha01")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
    implementation("androidx.room:room-runtime:2.3.0-alpha02")

    implementation("com.google.android.material:material:1.3.0-alpha02")
    implementation("com.google.firebase:firebase-firestore-ktx:21.5.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")

    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.72")
    testImplementation("junit:junit:4.13")
}
