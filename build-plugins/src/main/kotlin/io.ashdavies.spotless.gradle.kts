import com.diffplug.gradle.spotless.FormatExtension

plugins {
    id("com.diffplug.spotless")
}

spotless {
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

    ratchetFrom = "origin/main"
}
