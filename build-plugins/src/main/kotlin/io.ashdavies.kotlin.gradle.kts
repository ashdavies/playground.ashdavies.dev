import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

private val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

apply<DetektPlugin>()
apply<KtlintPlugin>()

kotlin {
    explicitApi()
    jvm()

    jvmToolchain(jvmTargetVersion.toInt())

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

extensions.configure<DetektExtension> {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("detekt-config.yml"))
}

extensions.configure<KtlintExtension> {
    /*additionalEditorconfig.putAll(
        mapOf(
            "ktlint_standard_trailing-comma-on-call-site" to "disabled",
            "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
        )
    )*/
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmTargetVersion
}
