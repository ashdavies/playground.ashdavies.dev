import ProjectDependencies.ApolloGraphQl
import ProjectDependencies.Dropbox
import ProjectDependencies.Google
import ProjectDependencies.JetBrains

plugins {
    `kotlin-jvm`
    serialization
    apollo
}

apollo {
    generateKotlinModels.set(true)
}

configurations.create("invoker")

dependencies {
    implementation(project(":shared"))

    implementation(ApolloGraphQl.apolloRuntime)
    implementation(ApolloGraphQl.apolloCoroutinesSupport)
    implementation(Dropbox.store4)
    implementation(Google.CloudFunctions.functionsFrameworkApi)
    implementation(Google.Firebase.firebaseAdmin)
    implementation(JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxSerializationCore)
    implementation(JetBrains.KotlinX.kotlinxSerializationJson)

    add("invoker", Google.CloudFunctions.javaFunctionInvoker)

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