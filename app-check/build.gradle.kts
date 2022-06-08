plugins {
    `cloud-function`
}

dependencies {
    implementation(project(":local-remote"))

    implementation(libs.auth.java.jwt)
    implementation(libs.bundles.ktor.client)
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.check.AppCheckFunction"
    description = "Deploy app check function to Google Cloud"
    allowUnauthenticated = true
    function = "appCheck"
}

val runAppCheckFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.check.AppCheckFunction"
    description = "Run app check cloud function"

    doFirst {
        sourceSets.main.configure {
            args("--classpath", files(configurations.runtimeClasspath, output).asPath)
        }
    }
}
