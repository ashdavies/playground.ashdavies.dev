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
            implementation(projects.localStorage)

            implementation(libs.jetbrains.kotlinx.serialization.properties)

            with(libs.ktor.client) {
                implementation(content.negotiation)
                implementation(core)
                implementation(json)
                implementation(logging)
                implementation(okhttp3)
            }

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
    inputSpec.set("$projectDir/../google-cloud/src/main/resources/openapi-v2.yml")
    packageName.set("io.ashdavies.playground")
    serverVariables.put("cloud_run_hostname", String())
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "multiplatform")
    configOptions.put("sourceFolder", ".")
}
