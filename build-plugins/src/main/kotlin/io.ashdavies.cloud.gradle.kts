import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose")
    id("com.github.johnrengelman.shadow")

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

kotlin.configureKotlinProject(project)

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = Playground.jvmTarget
}

tasks.named<ShadowJar>("shadowJar") {
    destinationDirectory.set(file("$buildDir/playground"))
    mergeServiceFiles()
}

