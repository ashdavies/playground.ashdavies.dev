import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    alias(libs.plugins.apollo.graphql)
    alias(libs.plugins.build.config)
}

apollo {
    generateKotlinModels.set(true)
}

buildConfig {
    val githubToken by stringPropertyOrNull { value ->
        buildConfigField<String?>("GITHUB_TOKEN", value)
    }

    packageName.set("io.ashdavies.playground.aggregator")
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled

    commonMain.dependencies {
        implementation(projects.microYaml)

        implementation(libs.apollo.graphql.coroutines.support)
        implementation(libs.apollo.graphql.runtime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.squareup.okhttp)
        implementation(libs.squareup.okio)
    }
}
