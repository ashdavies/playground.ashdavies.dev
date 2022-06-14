plugins {
    `cloud-function`
}

dependencies {
    implementation(libs.auth.java.jwt)
    implementation(libs.bundles.ktor.client)
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.check.AppCheckFunction"
    description = "Deploy app check function to Google Cloud"
    allowUnauthenticated = true
    function = "app-check"
}

val runAppCheckFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.check.AppCheckFunction"
    description = "Run app check cloud function"
    setupClasspath()
}

fun RunFunctionTask.setupClasspath() = doFirst {
    sourceSets.main.configure { args("--classpath", files(configurations.runtimeClasspath, output).asPath) }
}
