@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

plugins {
    id("com.google.cloud.tools.jib")
    kotlin("plugin.serialization")
    kotlin("jvm")
    application

    alias(libs.plugins.openapi.generator)
}

application {
    mainClass.set("io.ashdavies.cloud.MainKt")
}

dependencies {
    implementation(projects.appCheck.appCheckSdk)
    implementation(projects.cloudFirestore)
    implementation(projects.composeLocals)
    implementation(projects.localRemote)
    implementation(projects.localStorage)

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.bundles.ktor.serialization)
    implementation(libs.bundles.ktor.server)

    implementation(dependencies.platform(libs.google.cloud.bom))
    implementation(libs.google.cloud.firestore)
    implementation(libs.google.cloud.storage)
    implementation(libs.google.firebase.admin)

    testImplementation(kotlin("test"))

    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.server.test.host)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

sourceSets.main {
    java.srcDir(tasks.openApiGenerate)
}

openApiGenerate {
    generatorName.set("kotlin-server")
    outputDir.set("$buildDir/generated/openapi/main")
    remoteInputSpec.set("https://playground.ashdavies.dev/openapi/documentation.yml")
    templateDir.set("$projectDir/src/main/resources/templates")
    auth.set("X-API-KEY:${System.getenv("PLAYGROUND_API_KEY")}")
    packageName.set("io.ashdavies.playground")
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    configOptions.put("library", "ktor")
    configOptions.put("sourceFolder", ".")

    importMappings.put("LocalDate", "kotlinx.datetime.LocalDate")
    typeMappings.put("date", "LocalDate")
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=warning"
    kotlinOptions.jvmTarget = "11"
}
