plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    api(projects.microYaml)
    api(libs.apollo.graphql.api)
    api(libs.apollo.graphql.runtime)

    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okio)
}
