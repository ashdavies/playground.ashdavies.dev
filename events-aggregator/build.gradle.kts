plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    api(projects.microYaml)

    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okio)
}
