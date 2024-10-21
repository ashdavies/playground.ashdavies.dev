import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
    jvm()
}

private val detektAll by tasks.registering(Detekt::class) {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("detekt-config.yml"))
    exclude { "generated" in "$it" }
    setSource(files(projectDir))
    parallel = true
}

dependencies {
    add("detektPlugins", libs.detekt.compose)
}

extensions.configure<DetektExtension> {
    val detektPlugin = libs.plugins.detekt.get()
    toolVersion = "${detektPlugin.version}"
}


tasks.withType<KotlinCompile> {
    val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
        jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
    }
}

extensions.configure<KtlintExtension> {
    filter {
        exclude { "generated" in it.file.path }
    }

    val ktlintBom = libs.pinterest.ktlint.bom.get()
    version = ktlintBom.version
}
