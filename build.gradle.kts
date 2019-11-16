buildscript {
  repositories {
    configure()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.0.0-alpha03")
    classpath("com.google.gms:google-services:4.3.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.60-eap-143")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0")
  }
}

allprojects {
  repositories {
    configure()
  }
}
