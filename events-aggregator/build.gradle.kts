plugins {
    id("io.ashdavies.cloud")
    id("apollo.graphql")
}

dependencies {
    implementation(project(":events-function"))
}
