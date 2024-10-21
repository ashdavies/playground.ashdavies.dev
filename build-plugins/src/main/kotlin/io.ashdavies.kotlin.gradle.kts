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

private val detektAll by tasks.registering {
    val notEmptyDetektTasks = tasks
        .withType<Detekt>()
        .toMutableSet()
        .filter { it.source.files.isNotEmpty() }

    dependsOn(notEmptyDetektTasks)
}

dependencies {
    add("detektPlugins", libs.detekt.compose)
}

extensions.configure<DetektExtension> {
    config.setFrom(rootProject.file("detekt-config.yml"))
    buildUponDefaultConfig = true
    toolVersion = "1.23.7"
}

extensions.configure<KtlintExtension> {
    filter {
        exclude { "generated" in it.file.path }
    }

    val ktlintBom = libs.pinterest.ktlint.bom.get()
    version = ktlintBom.version
}

tasks.withType<Detekt> {
    val build by tasks.getting {
        dependsOn(this@withType)
    }

    exclude { "generated" in "$it" }
}

tasks.withType<KotlinCompile> {
    val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
        jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion))
    }
}
