buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }

    dependencies {
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
        androidx("6834848")

        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}
