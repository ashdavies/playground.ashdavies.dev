import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    jvm()

    val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

    tasks.withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs += "-Xexpect-actual-classes"
        kotlinOptions.jvmTarget = jvmTargetVersion
    }

    jvmToolchain(jvmTargetVersion.toInt())
}
