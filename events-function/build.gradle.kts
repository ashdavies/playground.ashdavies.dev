plugins {
    `cloud-function`
}

tasks.register<DeployFunctionTask>("deployEventsFunction") {
    entryPoint = "io.ashdavies.playground.events.EventsFunction"
    description = "Deploy events function to Google Cloud"
    allowUnauthenticated = true
}

val runEventsFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.playground.events.EventsFunction"
    description = "Run events cloud functions"

    doFirst {
        sourceSets.main.configure {
            args("--classpath", files(configurations.runtimeClasspath, output).asPath)
        }
    }
}
