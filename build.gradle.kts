import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

buildscript {
    dependencies {
        classpath(`batik-ext`)
        classpath(`kotlin-gradle-plugin`)
        classpath(gradle)
        classpath(sqldelight)
    }
}

plugins {
    ktlint
    versions
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
            "-XXLanguage:+InlineClasses",
        )
    }
}
