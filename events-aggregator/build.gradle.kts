plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    implementation(projects.cloudBackend.cloudFirestore)
    implementation(projects.cloudBackend.microYaml)
    implementation(projects.localStorage)

    testImplementation(libs.ktor.client.core)
}
