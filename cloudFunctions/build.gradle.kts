// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id(libs.plugins.kotlin.jvm)

    alias(libs.plugins.johnRengelman.shadow)
    alias(libs.plugins.serialization)
}

configurations.create("invoker")

dependencies {
    implementation(project(":localStorage"))

    implementation(libs.coroutineDispatcherCore)
    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)
    implementation(libs.jetbrains.kotlinx.coroutinesCore)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.jetbrains.kotlinx.serializationCore)
    implementation(libs.jetbrains.kotlinx.serializationJson)
    implementation(libs.jetbrains.kotlinx.serializationProperties)

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
