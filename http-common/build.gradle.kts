import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("io.ashdavies.kotlin")

    alias(libs.plugins.openapi.generator)
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled

    commonMain {
        dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
        }

        kotlin.srcDir(tasks.openApiGenerate)
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    globalProperties.set(mapOf("models" to ""))
    outputDir.set("$buildDir/generated/openapi/main")
    inputSpec.set("$rootDir/${property("openapi.generator.inputSpec")}")
    packageName.set("io.ashdavies.http.common")
    additionalProperties.put("dateLibrary", "string")
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "multiplatform")
    configOptions.put("sourceFolder", ".")
}

detekt {
    allRules = false
}
