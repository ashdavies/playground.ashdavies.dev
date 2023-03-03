plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    implementation(projects.cloudFirestore)
    implementation(projects.cloudFunctions)
    implementation(projects.localStorage)

    testImplementation(testFixtures(projects.cloudFunctions))
    testImplementation(libs.ktor.client.core)
}
