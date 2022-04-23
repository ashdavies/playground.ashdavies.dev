// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(libs.plugins.kotlin.jvm)
    id(libs.plugins.serialization)

    alias(libs.plugins.johnRengelman.shadow)
}

configurations.create("invoker")

dependencies {
    implementation(project(":local-storage"))

    implementation(libs.coroutine.dispatcher.core)
    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.jetbrains.kotlinx.serialization.core)
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.jetbrains.kotlinx.serialization.properties)

    add("invoker", libs.google.cloud.javaFunctionInvoker)

    testImplementation(kotlin("test"))
}

tasks.named<ShadowJar>("shadowJar") {
    destinationDirectory.set(file("$buildDir/playground"))
    mergeServiceFiles()
}

tasks.register<Exec>("deployEventsFunction") {
    dependsOn(tasks.named("shadowJar"))

    description = "Deploy events function to Google Cloud"
    workingDir = project.buildDir
    group = "deploy"

    commandLine = listOf(
        "gcloud", "functions", "deploy", "events",
        "--entry-point=io.ashdavies.playground.events.EventsFunction",
        "--project=playground-1a136",
        "--allow-unauthenticated",
        "--region=europe-west1",
        "--source=playground",
        "--runtime=java11",
        "--trigger-http"
    )
}

tasks.register("runEventsFunction", JavaExec::class) {
    dependsOn(tasks.named("compileKotlin"))
    description = "Run events cloud functions"
    group = "run"

    classpath(configurations.getByName("invoker"))
    mainClass.set("com.google.cloud.functions.invoker.runner.Invoker")

    sourceSets.main.configure {
        inputs.files(configurations.runtimeClasspath, output)
    }

    args("--target", "io.ashdavies.playground.events.EventsFunction")
    args("--port", 8080)

    doFirst {
        sourceSets.main.configure {
            args("--classpath", files(configurations.runtimeClasspath, output).asPath)
        }
    }
}
