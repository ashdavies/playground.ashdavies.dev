plugins {
    `cloud-function`
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.playground.check.AppCheckFunction"
    description = "Deploy app check function to Google Cloud"
    allowUnauthenticated = true
    function = "appCheck"
}

val runAppCheckFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.playground.check.AppCheckFunction"
    description = "Run app check cloud function"
}
