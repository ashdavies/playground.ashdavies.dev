plugins {
    `apollo-graphql`
    `cloud-function`
}

dependencies {
    implementation(project(":events-function"))
}

val deployEventsAggregatorFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.playground.aggregator.AggregatorFunction"
    description = "Deploy events aggregator function to Google Cloud"
    function = "aggregate"
}

val runEventsAggregatorFunction by tasks.registering(RunFunctionTask::class) {
    target = "io.ashdavies.playground.aggregator.AggregatorFunction"
    description = "Run events aggregator cloud function"

    doFirst {
        sourceSets.main.configure {
            args("--classpath", files(configurations.runtimeClasspath, output).asPath)
        }
    }
}

