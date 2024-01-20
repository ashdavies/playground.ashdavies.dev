import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

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

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmTargetVersion
}
