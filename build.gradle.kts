import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

buildscript {
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
    }
}

subprojects {
    tasks.withType<KotlinCompile<*>> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xallow-result-return-type",
        )
    }
}
