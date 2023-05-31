@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

import com.diffplug.gradle.spotless.FormatExtension

buildscript {
    dependencies {
        val googleServicesVersion = libs.versions.google.services.get()
        classpath("com.google.gms:google-services:$googleServicesVersion")
        classpath("com.jakewharton.mosaic:mosaic-gradle-plugin:0.6.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.apollo.graphql) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.diffplug.spotless)
    alias(libs.plugins.gradle.doctor)

    // alias(libs.plugins.cash.molecule)
    // alias(libs.plugins.kotlinx.kover)
}

doctor {
    allowBuildingAllAndroidAppsSimultaneously.set(true)
    disallowCleanTaskDependencies.set(false)
    javaHome { failOnError.set(false) }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    javascript {
        target("**/*.js")
        prettier()
    }

    val ktLintVersion = libs.versions.pinterest.ktlint.get()
    fun FormatExtension.kotlinDefault(extension: String = "kt") {
        target("**/src/**/*.$extension")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        kotlinDefault("gradle.kts")
        ktlint(ktLintVersion)
    }

    kotlin {
        val editorConfig = mapOf(
            "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
            "ij_kotlin_allow_trailing_comma" to "true",
            "disabled_rules" to "filename",
            "experimental" to "true",
            "android" to "true",
        )

        ktlint(ktLintVersion)
            .editorConfigOverride(editorConfig)
            .setUseExperimental(true)

        kotlinDefault()
    }

    format("misc") {
        target(".gitignore", "*.md", "*.toml")
        trimTrailingWhitespace()
        endWithNewline()
    }

    val terraformPath = System.getenv("TERRAFORM_PATH")
    if (terraformPath != null) format("terraform") {
        val terraformExe = "$terraformPath/terraform_1.3.1"
        nativeCmd("terraform", terraformExe, listOf("fmt", "-"))
        target("src/main/terraform/**/*.tf")
    }

    ratchetFrom = "origin/main"
}
