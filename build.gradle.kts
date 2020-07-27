buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(BuildPlugins.BuildTools)
        classpath(BuildPlugins.GoogleServices)
        classpath(BuildPlugins.Kotlin)
        classpath(BuildPlugins.OssLicenses)
        classpath(BuildPlugins.SafeArgs)
        classpath(BuildPlugins.Versioning)
    }
}

plugins {
    id("com.github.ben-manes.versions") version BuildPlugins.Versions.version
    id("com.osacky.doctor") version BuildPlugins.GradleDoctor.version
    id("io.gitlab.arturbosch.detekt") version BuildPlugins.Detekt.version
    id("org.jlleitschuh.gradle.ktlint") version BuildPlugins.KtLint.version
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}
