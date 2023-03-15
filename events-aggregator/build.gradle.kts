plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    implementation(projects.cloudFirestore)
    implementation(projects.localStorage)

    testImplementation(libs.ktor.client.core)
}
