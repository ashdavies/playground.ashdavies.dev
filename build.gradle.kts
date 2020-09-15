buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(BuildPlugins.Gradle)
        classpath(BuildPlugins.GoogleServices)
        classpath(BuildPlugins.KotlinGradlePlugin)
        classpath(BuildPlugins.OssLicensesPlugin)
        classpath(BuildPlugins.NavigationSafeArgsGradlePlugin)
        classpath(BuildPlugins.AndroidVersioning)
    }
}

plugins {
    id("com.github.ben-manes.versions") version BuildPlugins.GradleVersionsPlugin.version
    id("com.osacky.doctor") version BuildPlugins.DoctorPlugin.version
    id("io.gitlab.arturbosch.detekt") version BuildPlugins.DetektGradlePlugin.version
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
