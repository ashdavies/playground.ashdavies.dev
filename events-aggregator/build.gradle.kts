import com.apollographql.apollo.gradle.internal.ApolloDownloadSchemaTask
import com.apollographql.apollo.gradle.internal.ApolloGenerateSourcesTask
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

private val githubToken: String? by stringPropertyOrNull()

object GraphEndpoints {
    const val GITHUB = "https://api.github.com/graphql"
}

plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    alias(libs.plugins.apollo.graphql)
    alias(libs.plugins.build.config)
}

apollo {
    service("github") {
        introspection {
            schemaFile.set(file("src/commonMain/graphql/io/ashdavies/github/schema.graphqls"))
            headers.put("Authorization", "Bearer ${requireNotNull(githubToken)}")
            endpointUrl.set(GraphEndpoints.GITHUB)
        }

        //mapScalar()

        packageName = "io.ashdavies.github"
    }
}

buildConfig {
    buildConfigField("GITHUB_GRAPHQL_SERVER_URL", GraphEndpoints.GITHUB)
    buildConfigField("GITHUB_TOKEN", githubToken)

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

val downloadGithubApolloSchemaFromIntrospection by tasks.getting(ApolloDownloadSchemaTask::class)
val generateGithubApolloSources by tasks.getting(ApolloGenerateSourcesTask::class) {
    dependsOn(downloadGithubApolloSchemaFromIntrospection)
}
