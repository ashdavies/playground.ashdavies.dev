plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.graphql")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

buildConfig {
    val githubToken by stringPropertyOrNull { value ->
        buildConfigField<String?>("GITHUB_TOKEN", value)
    }

    packageName.set("io.ashdavies.playground.aggregator")
}

dependencies {
    implementation(projects.microYaml)

    implementation(libs.apollo.graphql.coroutines.support)
    implementation(libs.apollo.graphql.runtime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okio)
}
