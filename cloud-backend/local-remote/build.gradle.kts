@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

plugins {
    id("io.ashdavies.default")

    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "io.ashdavies.local.remote"
}

kotlin {
    explicitApiWarning()

    commonMain {
        dependencies {
            implementation(projects.localStorage)

            implementation(libs.bundles.ktor.client)
            implementation(libs.bundles.ktor.serialization)

            implementation(libs.jetbrains.kotlinx.serialization.properties)
        }

        kotlin.srcDir(tasks.openApiGenerate)
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    outputDir.set("$buildDir/generated/openapi/main")
    inputSpec.set("$projectDir/../google-cloud/src/main/resources/openapi-v2.yml")
    packageName.set("io.ashdavies.playground")
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "multiplatform")
    configOptions.put("sourceFolder", ".")
}
