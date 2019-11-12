plugins {
  id("com.android.library")

  id("kotlin-android")
  id("kotlin-kapt")
}

android {
  setCompileSdkVersion(29)

  compileOptions {
    setSourceCompatibility(1.8)
    setTargetCompatibility(1.8)
  }

  defaultConfig {
    setMinSdkVersion(21)
    setTargetSdkVersion(29)
  }

  /*sourceSets {
    main
        .java
        .srcDirs("src/main/kotlin")

    test
        .java
        .srcDirs("src/test/kotlin")
  }*/
}

dependencies {
  implementation("androidx.annotation:annotation:1.1.0")
  implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0-rc01")
  implementation("androidx.databinding:databinding-runtime:4.0.0-alpha01")
  implementation("androidx.lifecycle:lifecycle-livedata-core:2.2.0-rc01")
  implementation("androidx.room:room-runtime:2.2.1")

  implementation("com.google.android.material:material:1.2.0-alpha01")
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.50")

  testImplementation("com.google.truth:truth:1.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
}
