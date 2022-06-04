plugins {
    `cloud-function`
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.playground.events.EventsFunction"
    description = "Deploy events function to Google Cloud"
    allowUnauthenticated = true
}

val runAppCheckFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.playground.events.EventsFunction"
    description = "Run events cloud functions"
}
