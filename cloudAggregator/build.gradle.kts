// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id(libs.plugins.apollo)
    id(libs.plugins.kotlin.jvm)

    alias(libs.plugins.johnRengelman.shadow)
    alias(libs.plugins.serialization)
}

apollo {
    generateKotlinModels.set(true)
}

configurations.create("invoker")

dependencies {
    implementation(project(":cloudFunctions"))
    implementation(project(":shared"))

    implementation(libs.apolloGraphQl.apolloRuntime)
    implementation(libs.apolloGraphQl.apolloCoroutinesSupport)
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

tasks.register<Exec>("deployAggregatorFunction") {
    dependsOn(tasks.named("shadowJar"))

    description = "Deploy aggregator function to Google Cloud"
    workingDir = project.buildDir
    group = "deploy"

    commandLine = listOf(
        "gcloud", "functions", "deploy", "events",
        "--entry-point=io.ashdavies.playground.aggregator.AggregatorFunction",
        "--project=playground-1a136",
        "--region=europe-west1",
        "--source=playground",
        "--runtime=java11",
        "--trigger-http"
    )
}

tasks.register("runAggregatorFunction", JavaExec::class) {
    dependsOn(tasks.named("compileKotlin"))
    description = "Run events cloud functions"
    group = "run"

    classpath(configurations.getByName("invoker"))
    mainClass.set("com.google.cloud.functions.invoker.runner.Invoker")

    sourceSets.main.configure {
        inputs.files(configurations.runtimeClasspath, output)
    }

    args("--target", "io.ashdavies.playground.aggregator.AggregatorFunction")
    args("--port", 8080)

    doFirst {
        sourceSets.main.configure {
            args("--classpath", files(configurations.runtimeClasspath, output).asPath)
        }
    }
}
