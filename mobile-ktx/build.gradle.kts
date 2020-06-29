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
        main.srcDirs = setOf("src/main/kotlin")
        test.srcDirs = setOf("src/test/kotlin")
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.2.0-alpha01")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.databinding:databinding-runtime:4.2.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
    implementation("androidx.room:room-runtime:2.3.0-alpha01")

    implementation("com.google.android.material:material:1.3.0-alpha01")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4-M2")

    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0-M1")
}
