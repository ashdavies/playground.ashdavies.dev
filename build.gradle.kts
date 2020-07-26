buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0")
        classpath("com.android.tools.build:gradle:4.2.0-alpha05")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.28.0")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.2")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("eu.appcom.gradle:android-versioning:1.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
    }
}

subprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}
