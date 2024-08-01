import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    alias(libs.plugins.apollo.graphql)
    alias(libs.plugins.build.config)
}

apollo {
    service("github") {
        packageName = "io.ashdavies.github"
    }
}

buildConfig {
    val githubToken by stringPropertyOrNull { value ->
        buildConfigField<String?>("GITHUB_TOKEN", value)
    }

    packageName.set("io.ashdavies.playground.aggregator")
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled

    sourceSets.commonMain.dependencies {
        implementation(projects.microYaml)
        implementation(libs.apollo.graphql.runtime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.squareup.okhttp)
        implementation(libs.squareup.okio)
    }
}
