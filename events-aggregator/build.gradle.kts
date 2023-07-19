plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    implementation(projects.cloudFirestore)
    implementation(projects.localStorage)
    implementation(projects.microYaml)

    testImplementation(libs.ktor.client.core)
}
