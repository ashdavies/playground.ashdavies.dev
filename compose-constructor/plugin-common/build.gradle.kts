import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")

    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(libs.jetbrains.kotlin.compiler.embeddable)
    compileOnly(libs.google.auto.service.annotations)
    kapt(libs.google.auto.service)

    testImplementation(kotlin("test"))

    testImplementation(libs.jetbrains.kotlin.compiler.embeddable)
    testImplementation(libs.tschuchortdev.kotlin.compile.testing)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
