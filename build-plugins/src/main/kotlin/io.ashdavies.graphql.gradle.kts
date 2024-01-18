import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.apollographql.apollo")
    kotlin("jvm")
}

apollo {
    generateKotlinModels.set(true)
}

dependencies {
    api(libs.apollo.graphql.runtime)
    api(libs.apollo.graphql.api)

    implementation(libs.apollo.graphql.coroutines.support)
}
