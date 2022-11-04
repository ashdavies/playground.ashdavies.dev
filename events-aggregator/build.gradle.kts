plugins {
    id("io.ashdavies.cloud")
    id("apollo.graphql")
}

dependencies {
    implementation(projects.cloudFunctions)
    implementation(projects.eventsFunction)
    implementation(projects.localStorage)
}
