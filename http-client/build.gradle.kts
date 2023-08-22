@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("io.ashdavies.default")

    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "io.ashdavies.local.remote"
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled

    commonMain {
        dependencies {
            with(libs.ktor.client) {
                api(cio)
                api(core)
                api(logging)
            }

            implementation(projects.localStorage)

            implementation(libs.jetbrains.kotlinx.serialization.properties)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.serialization.kotlinx)
            implementation(libs.slf4j.simple)
        }

        kotlin.srcDir(tasks.openApiGenerate)
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    outputDir.set("$buildDir/generated/openapi/main")
    inputSpec.set("$rootDir/${stringProperty("openapi.generator.inputSpec")}")
    packageName.set("io.ashdavies.generated")
    serverVariables.put("cloud_run_hostname", String())
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "multiplatform")
    configOptions.put("sourceFolder", ".")
}
