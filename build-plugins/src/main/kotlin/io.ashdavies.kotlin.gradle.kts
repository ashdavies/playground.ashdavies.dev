import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

apply<DetektPlugin>()

kotlin {
    explicitApi()
    jvm()

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
    config.setFrom(rootProject.file("detekt-config.yml"))
    buildUponDefaultConfig = true
    toolVersion = "1.23.5"
}

tasks.withType<Detekt> {
    val build by tasks.getting {
        dependsOn(this@withType)
    }

    exclude { "generated" in "$it" }
}

tasks.withType<KotlinCompile> {
    val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()
    kotlinOptions.jvmTarget = jvmTargetVersion
}
