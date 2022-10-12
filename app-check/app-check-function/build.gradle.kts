plugins {
    id("io.ashdavies.cloud")
    id("deploy-function")
}

dependencies {
    implementation(project(":app-check:app-check-compose"))
    implementation(project(":app-check:app-check-sdk"))

    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.google.cloud.javaFunctionInvoker)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.check.AppCheckFunction"
    description = "Deploy app check function to Google Cloud"
    allowUnauthenticated = true
    function = "createToken"

    envVar("APP_CHECK_KEY")
}
