@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage") // https://youtrack.jetbrains.com/issue/KTIJ-19369

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
    kotlin("plugin.serialization")
    kotlin("jvm")
}

configurations.create("invoker")

dependencies {
    implementation(project(":cloud-functions"))
    implementation(project(":local-storage"))

    implementation(libs.coroutine.dispatcher.core)

    with(libs.jetbrains.kotlinx) {
        implementation(coroutines.core)
        implementation(datetime)

        with(serialization) {
            implementation(core)
            implementation(json)
            implementation(properties)
        }
    }

    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)

    testImplementation(kotlin("test"))

    add("invoker", libs.google.cloud.javaFunctionInvoker)
}

/*kotlin {
    configureKotlinProject(project)
}*/

tasks.named<ShadowJar>("shadowJar") {
    destinationDirectory.set(file("$buildDir/playground"))
    mergeServiceFiles()
}
