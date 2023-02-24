plugins {
    id("com.google.cloud.tools.jib")
    kotlin("plugin.serialization")
    kotlin("jvm")
    application
}

application {
    mainClass.set("io.ashdavies.cloud.MainKt")
}

dependencies {
    implementation(projects.appCheck.appCheckSdk)
    implementation(projects.composeLocals)
    implementation(projects.localRemote)

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.bundles.ktor.serialization)
    implementation(libs.bundles.ktor.server)
    implementation(libs.google.firebase.admin)
    implementation(libs.ktor.server.openapi)
    implementation(libs.swagger.codegen.generator)

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

tasks.filter { it.name in setOf("jibDockerBuild", "jibBuildTar", "jib") }.onEach {
    it.notCompatibleWithConfigurationCache("Jib is not compatible with configuration cache")
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=warning"
    kotlinOptions.jvmTarget = "11"
}
