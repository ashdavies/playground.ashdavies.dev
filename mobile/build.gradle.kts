import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.application")

  kotlin("android")
  kotlin("android.extensions")
  kotlin("kapt")

  id("androidx.navigation.safeargs.kotlin")
}

android {
  setCompileSdkVersion(29)

  compileOptions {
    setSourceCompatibility(1.8)
    setTargetCompatibility(1.8)
  }

  defaultConfig {
    applicationId = "io.ashdavies.playground"

    setMinSdkVersion(21)
    setTargetSdkVersion(29)

    versionCode = 3
    versionName = "3.0"

    vectorDrawables.useSupportLibrary = true
  }

  dataBinding {
    isEnabled = true
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

  implementation("androidx.activity:activity-ktx:1.1.0-rc02")
  implementation("androidx.annotation:annotation:1.1.0")
  implementation("androidx.appcompat:appcompat:1.1.0")
  implementation("androidx.cardview:cardview:1.0.0")
  implementation("androidx.constraintlayout:constraintlayout:1.1.3")
  implementation("androidx.core:core-ktx:1.2.0-beta02")
  implementation("androidx.fragment:fragment-ktx:1.2.0-rc02")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0-rc02")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-rc02")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-rc02")
  implementation("androidx.paging:paging-runtime:2.1.0")
  implementation("androidx.room:room-ktx:2.2.1")
  implementation("androidx.navigation:navigation-fragment-ktx:2.2.0-rc02")
  implementation("androidx.navigation:navigation-ui-ktx:2.2.0-rc02")
  implementation("androidx.recyclerview:recyclerview:1.1.0-rc01")

  implementation("com.github.ashdavies:lifecycle-flow:0.7.2")
  implementation("com.google.android.material:material:1.2.0-alpha01")
  implementation("com.google.firebase:firebase-common-ktx:19.3.0")
  implementation("com.google.firebase:firebase-analytics:17.2.1")
  implementation("com.google.firebase:firebase-firestore-ktx:21.3.0")

  implementation("com.squareup.moshi:moshi:1.9.1")
  implementation("com.squareup.okhttp3:logging-interceptor:4.2.2")
  implementation("com.squareup.okhttp3:okhttp:4.2.2")
  implementation("com.squareup.retrofit2:retrofit:2.6.2")
  implementation("com.squareup.retrofit2:converter-moshi:2.6.2")

  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.60-eap-143")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.2")
  implementation("org.reduxkotlin:redux-kotlin-jvm:0.2.6")

  kapt("androidx.room:room-compiler:2.2.1")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.1")

  testImplementation("com.google.truth:truth:1.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
}

tasks
    .withType<KotlinCompile>()
    .configureEach {
      kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

apply(plugin = "com.google.gms.google-services")
