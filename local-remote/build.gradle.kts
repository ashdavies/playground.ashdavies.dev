@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

import com.android.build.api.dsl.VariantDimension
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("io.ashdavies.default")

    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.undercouch.download)
}

android {
    namespace = "io.ashdavies.local.remote"

    defaultConfig {
        val clientName by SystemProperty(VariantDimension::buildConfigField) { "Ktor/${libs.versions.ktor.get()}" }
    }
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)

        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.ktor.serialization)
    }
}

openApiGenerate {
    inputSpec.set("$buildDir/openapi/config.json")
    generatorName.set("kotlin")
}

val downloadOpenApiDocumentation by tasks.registering(Download::class) {
    header("X-API-KEY", System.getenv("PLAYGROUND_API_KEY"))
    src("https://playground.ashdavies.dev/openapi/documentation.yml")
    dest("$buildDir/openapi/config.yml")
    onlyIfModified(true)
    useETag(true)
}
