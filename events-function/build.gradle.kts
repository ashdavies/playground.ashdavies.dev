plugins {
    id("io.ashdavies.cloud")
    id("deploy-function")
}

dependencies {
    implementation(project(":app-check:app-check-sdk"))
}

tasks.register<DeployFunctionTask>("deployEventsFunction") {
    entryPoint = "io.ashdavies.playground.events.EventsFunction"
    description = "Deploy events function to Google Cloud"
    allowUnauthenticated = true
    function = "events"
}
