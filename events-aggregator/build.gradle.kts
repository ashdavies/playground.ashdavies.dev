plugins {
    `apollo-graphql`
    `cloud-function`
}

dependencies {
    implementation(project(":events-function"))
}

val deployEventsAggregatorFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.playground.aggregator.AggregatorFunction"
    description = "Deploy aggregator function to Google Cloud"
}

val runEventsAggregatorFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.playground.aggregator.AggregatorFunction"
    description = "Run events cloud functions"
}

