plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
}

dependencies {
    implementation(projects.microYaml)

    implementation(compose.runtime)

    implementation(libs.apollo.graphql.coroutines.support)
    implementation(libs.apollo.graphql.runtime)
    implementation(libs.jetbrains.kotlinx.serialization.core)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okio)
}
