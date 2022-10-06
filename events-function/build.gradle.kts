plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(project(":app-check"))
}

tasks.register<DeployFunctionTask>("deployEventsFunction") {
    entryPoint = "io.ashdavies.playground.events.EventsFunction"
    description = "Deploy events function to Google Cloud"
    allowUnauthenticated = true
    function = "events"
}
