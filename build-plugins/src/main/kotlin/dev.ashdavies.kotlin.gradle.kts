import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

apply<DetektPlugin>()
apply<KtlintPlugin>()

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget = libs.versions.kotlin.jvmTarget
                .map(JvmTarget::fromTarget)
                .get()
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("androidJvm") {
                withAndroidTarget()
                withJvm()
            }
        }
    }
}

private val detektAll by tasks.registering(Detekt::class) {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("detekt-config.yml"))
    include("**/io/ashdavies/**")
    exclude("**/generated/**")
    setSource(files(projectDir))
    parallel = true
}

dependencies {
    add("detektPlugins", libs.detekt.compose)
}

extensions.configure<DetektExtension> {
    toolVersion = libs.plugins.detekt
        .map { "${it.version}" }
        .get()
}

extensions.configure<KtlintExtension> {
    filter {
        exclude { "generated" in it.file.path }
    }

    version = libs.pinterest.ktlint.bom
        .map(Dependency::getVersion)
        .get()
}
