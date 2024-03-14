import io.gitlab.arturbosch.detekt.Detekt
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

private val detektAll by tasks.registering {
    dependsOn(tasks.withType<Detekt>())
}

extensions.configure<DetektExtension> {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("detekt-config.yml"))
}

extensions.configure<KtlintExtension> {
    filter {
        exclude { "generated" in it.file.path }
    }

    val ktlintBom = libs.pinterest.ktlint.bom.get()
    version = ktlintBom.version
}

tasks.withType<Detekt> {
    /*val build by tasks.getting {
        dependsOn(this@withType)
    }*/

    exclude { "generated" in "$it" }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmTargetVersion
}
