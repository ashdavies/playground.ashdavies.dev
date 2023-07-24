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
    jvm { withJava() }

    jvmMain.dependencies {
        with(projects) {
            implementation(appCheck.appCheckSdk)
            implementation(cloudFirestore)
            implementation(composeLocals)
            implementation(httpClient)
            implementation(eventsAggregator)
            implementation(localStorage)
        }

        with(libs.google) {
            implementation(dependencies.platform(cloud.bom))
            implementation(cloud.firestore)
            implementation(cloud.storage)

            implementation(firebase.admin)
        }

        with(libs.jetbrains.kotlinx) {
            implementation(collections.immutable)
            implementation(coroutines.core)
            implementation(datetime)
            implementation(serialization.core)
            implementation(serialization.json)
        }

        implementation(libs.ktor.serialization.json)
        implementation(libs.ktor.serialization.kotlinx)

        with(libs.ktor.server) {
            implementation(auth)
            implementation(call.logging)
            implementation(cio)
            implementation(compression)
            implementation(conditional.headers)
            implementation(content.negotiation)
            implementation(core)
            implementation(default.headers)
            implementation(request.validation)
        }
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
