plugins {
    id("io.ashdavies.terraform")
}

dependencies {
    lambda(projects.appCheck.appCheckFunction)
    lambda(projects.eventsAggregator)
}
