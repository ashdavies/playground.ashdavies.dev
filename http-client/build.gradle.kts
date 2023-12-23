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
            api(libs.ktor.client.core)
            api(libs.ktor.client.logging)

            implementation(projects.localStorage)
            implementation(libs.jetbrains.kotlinx.serialization.properties)

            with(libs.ktor.client) {
                implementation(content.negotiation)
                implementation(cio)
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
    inputSpec.set("$rootDir/${property("openapi.generator.inputSpec")}")
    templateDir.set("$projectDir/src/commonMain/resources/templates")
    packageName.set("io.ashdavies.generated")
    additionalProperties.put("dateLibrary", "kotlinx-datetime")
    serverVariables.put("cloud_run_hostname", String())
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "multiplatform")
    configOptions.put("sourceFolder", ".")
}
