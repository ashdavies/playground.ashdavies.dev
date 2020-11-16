buildscript {
    repositories {
        google()
        maven("https://kotlin.bintray.com/kotlinx/")
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(BuildPlugins.Batik)
        classpath(BuildPlugins.Gradle)
        classpath(BuildPlugins.KotlinGradlePlugin)
        classpath(BuildPlugins.SqlDelight)
    }
}

plugins {
    id("com.github.ben-manes.versions") version BuildPlugins.GradleVersionsPlugin.version
    id("org.jlleitschuh.gradle.ktlint") version BuildPlugins.KtlintGradle.version
}

allprojects {
    repositories {
        google()
        maven("https://kotlin.bintray.com/kotlinx/")
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}
