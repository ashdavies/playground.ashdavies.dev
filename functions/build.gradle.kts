// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.apollo)
    id(libs.plugins.kotlin.jvm)

    alias(libs.plugins.serialization)
}

apollo {
    generateKotlinModels.set(true)
}

configurations.create("invoker")

dependencies {
    implementation(project(":shared"))

    implementation(libs.apolloGraphQl.apolloRuntime)
    implementation(libs.apolloGraphQl.apolloCoroutinesSupport)
    implementation(libs.dropbox.mobile.store)
    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)
    implementation(libs.jetbrains.kotlinx.coroutinesCore)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.jetbrains.kotlinx.serializationCore)
    implementation(libs.jetbrains.kotlinx.serializationJson)

    add("invoker", libs.google.cloud.javaFunctionInvoker)

    testImplementation(kotlin("test"))
}

tasks.register("runEventsFunction", JavaExec::class) {
    description = "Run events cloud functions"
    dependsOn("compileKotlin")
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
