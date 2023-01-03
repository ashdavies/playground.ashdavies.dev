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
    implementation(projects.appCheck.appCheckSdk)
    implementation(projects.composeLocals)
    implementation(projects.localRemote)

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.bundles.ktor.serialization)
    implementation(libs.bundles.ktor.server)
    implementation(libs.google.firebase.admin)
    implementation("io.ktor:ktor-server-compression-jvm:2.1.3")
    // implementation(libs.qos.logbackClassic)

    testImplementation(kotlin("test"))
    testImplementation(projects.appCheck.appCheckSdk)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.server.test.host)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=warning"
}

