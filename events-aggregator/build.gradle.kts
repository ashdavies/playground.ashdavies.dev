plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    with(projects) {
        implementation(cloudFirestore)
        implementation(localStorage)
        implementation(microYaml)
    }

    implementation(libs.google.guava.jre)
}
