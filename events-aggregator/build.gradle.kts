plugins {
    id("io.ashdavies.cloud")
    id("apollo.graphql")
    id("deploy-function")
}

dependencies {
    implementation(project(":events-function"))
}

val deployEventsAggregatorFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.playground.aggregator.AggregatorFunction"
    description = "Deploy events aggregator function to Google Cloud"
    function = "aggregate"
}
