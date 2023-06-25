@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

import com.diffplug.gradle.spotless.FormatExtension

buildscript {
    dependencies {
        val googleServicesVersion = libs.versions.google.services.get()
        classpath("com.google.gms:google-services:$googleServicesVersion")
    }
}

plugins {
    fun classpath(notation: Provider<PluginDependency>) = alias(notation) apply false

    with(libs.plugins) {
        classpath(android.application)
        classpath(android.library)
        classpath(apollo.graphql)
        classpath(google.services)

        with(kotlin) {
            classpath(compose)
            classpath(multiplatform)
            classpath(serialization)
        }

        alias(dependency.analysis)
        alias(diffplug.spotless)
        alias(gradle.doctor)

        // alias(cash.molecule)
        // alias(kotlinx.kover)
    }
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
