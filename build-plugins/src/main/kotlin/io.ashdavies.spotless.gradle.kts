import com.diffplug.gradle.spotless.FormatExtension

plugins {
    id("com.diffplug.spotless")
}

spotless {
    val ktLintVersion = libs.versions.pinterest.ktlint.get()
    val editorConfig = mapOf(
        "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
        "ktlint_standard_function-naming" to "disabled",
        "ktlint_standard_property-naming" to "disabled",
        "ij_kotlin_allow_trailing_comma" to "true",
        "ktlint_standard_filename" to "disabled",
        "ktlint_experimental" to "enabled",
        "android" to "true",
    )

    fun FormatExtension.kotlinDefault(target: String) {
        targetExclude("**/build/**")
        target("**/src/**/$target")
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        ktlint(ktLintVersion).editorConfigOverride(editorConfig)
        kotlinDefault("*.gradle.kts")
    }

    kotlin {
        ktlint(ktLintVersion).editorConfigOverride(editorConfig)
        kotlinDefault("*.kt")
    }

    format("misc") {
        target(".gitignore", "*.md", "*.toml")
        trimTrailingWhitespace()
        endWithNewline()
    }

    ratchetFrom = "origin/main"
}
