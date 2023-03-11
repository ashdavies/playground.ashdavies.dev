@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

import com.android.build.api.dsl.VariantDimension

plugins {
    id("io.ashdavies.default")

    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "io.ashdavies.local.remote"

    defaultConfig {
        val clientName by SystemProperty(VariantDimension::buildConfigField) { "Ktor/${libs.versions.ktor.get()}" }
    }
}

kotlin.commonMain {
    dependencies {
        implementation(projects.localStorage)

        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.ktor.serialization)

        implementation(libs.jetbrains.kotlinx.serialization.properties)
    }

    kotlin.srcDir(tasks.openApiGenerate)
}

openApiGenerate {
    generatorName.set("kotlin")
    outputDir.set("$buildDir/generated/openapi/main")
    remoteInputSpec.set("https://playground.ashdavies.dev/openapi/documentation.yml")
    auth.set("X-API-KEY:${System.getenv("PLAYGROUND_API_KEY")}")
    packageName.set("io.ashdavies.playground")
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "multiplatform")
    configOptions.put("sourceFolder", ".")
}
