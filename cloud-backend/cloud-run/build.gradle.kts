@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

plugins {
    id("com.google.cloud.tools.jib")
    id("io.ashdavies.integration")
    id("io.ashdavies.kotlin")
    application

    alias(libs.plugins.openapi.generator)
}

application {
    mainClass.set("io.ashdavies.cloud.MainKt")
}

jib {
    container.mainClass = "MainKt"
}

kotlin {
    explicitApiWarning()

    jvmMain.dependencies {
        implementation(projects.appCheck.appCheckSdk)
        implementation(projects.cloudBackend.cloudFirestore)
        implementation(projects.composeLocals)
        implementation(projects.eventsAggregator)
        implementation(projects.cloudBackend.localRemote)
        implementation(projects.localStorage)

        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.bundles.ktor.serialization)
        implementation(libs.bundles.ktor.server)

        implementation(dependencies.platform(libs.google.cloud.bom))
        implementation(libs.google.cloud.firestore)
        implementation(libs.google.cloud.storage)
        implementation(libs.google.firebase.admin)
    }

    jvmIntegrationTest.dependencies {
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.server.test.host)
    }
}

sourceSets.main {
    java.srcDir(tasks.openApiGenerate)
}

openApiGenerate {
    generatorName.set("kotlin-server")
    outputDir.set("$buildDir/generated/openapi/main")
    inputSpec.set("$projectDir/../google-cloud/src/main/resources/openapi-v2.yml")
    templateDir.set("$projectDir/src/jvmMain/resources/templates")
    packageName.set("io.ashdavies.playground")
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "ktor")
    configOptions.put("sourceFolder", ".")
    typeMappings.put("date", "LocalDate")
    importMappings.put("LocalDate", "kotlinx.datetime.LocalDate")
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}
