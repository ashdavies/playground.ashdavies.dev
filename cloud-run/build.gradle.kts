@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

plugins {
    alias(libs.plugins.google.cloud.jib)
    kotlin("plugin.serialization")
    kotlin("jvm")
    application
}

application {
    mainClass.set("io.ashdavies.cloud.MainKt")
}

dependencies {
    implementation(projects.appCheck.appCheckFunction)
    implementation(projects.composeLocals)
    implementation(projects.eventsAggregator)
    implementation(projects.eventsFunction)

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.bundles.ktor.serialization)
    implementation(libs.bundles.ktor.server)
    implementation(libs.google.firebase.admin)
    implementation(libs.qos.logbackClassic)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=warning"
}
