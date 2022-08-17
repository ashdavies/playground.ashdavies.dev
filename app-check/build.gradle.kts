import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    // id("compose-constructor")
    `cloud-function`
}

dependencies {
    implementation(project(":compose-constructor:plugin-runtime"))

    implementation(libs.auth.java.jwt)
    implementation(libs.auth.jwks.rsa)

    implementation(libs.bundles.ktor.client)
    implementation(libs.ktor.client.auth)

    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.google.cloud.javaFunctionInvoker)
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.check.AppCheckFunction"
    description = "Deploy app check function to Google Cloud"
    allowUnauthenticated = true
    function = "createToken"

    envVar("APP_CHECK_KEY")
}

/*configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.ashdavies.playground:plugin-common")).using(project(":compose-constructor:plugin-common"))
        substitute(module("io.ashdavies.playground:plugin-gradle")).using(project(":compose-constructor:plugin-gradle"))
        substitute(module("io.ashdavies.playground:plugin-ide")).using(project(":compose-constructor:plugin-ide"))
        substitute(module("io.ashdavies.playground:plugin-native")).using(project(":compose-constructor:plugin-native"))
        substitute(module("io.ashdavies.playground:plugin-runtime")).using(project(":compose-constructor:plugin-runtime"))
    }
}*/
