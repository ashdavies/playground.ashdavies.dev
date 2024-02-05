import com.diffplug.gradle.spotless.FormatExtension

plugins {
    id("com.diffplug.spotless")
}

spotless {
    val ktlintVersion = libs.versions.pinterest.ktlint.get()
    val composeKtlint = libs.compose.ktlint.get()

    val editorConfig = mapOf(
        "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
        "ij_kotlin_allow_trailing_comma" to "true",
    )

    fun FormatExtension.kotlinDefault(target: String) {
        targetExclude("**/build/**")
        target("**/src/**/$target")
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        ktlint(ktlintVersion)
            .customRuleSets(listOf("$composeKtlint"))
            .editorConfigOverride(editorConfig)

        kotlinDefault("*.gradle.kts")
    }

    kotlin {
        ktlint(ktlintVersion)
            .customRuleSets(listOf("$composeKtlint"))
            .editorConfigOverride(editorConfig)

        kotlinDefault("*.kt")
    }

    format("misc") {
        target(".gitignore", "*.md", "*.toml")
        trimTrailingWhitespace()
        endWithNewline()
    }

    ratchetFrom = "origin/main"
}
