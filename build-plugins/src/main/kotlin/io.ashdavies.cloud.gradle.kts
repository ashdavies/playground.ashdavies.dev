import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose")

    kotlin("plugin.serialization")
    kotlin("jvm")
}

configurations.create("invoker")

dependencies {
    implementation(compose.foundation)
    implementation(compose.runtime)

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)

    testImplementation(kotlin("test"))
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)

    add("invoker", libs.google.cloud.javaFunctionInvoker)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = "${Playground.jvmTarget}"
}
