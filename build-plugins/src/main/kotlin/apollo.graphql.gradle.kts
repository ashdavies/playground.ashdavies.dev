import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.apollographql.apollo")
    kotlin("jvm")
}

apollo {
    generateKotlinModels.set(true)
}

dependencies {
    implementation(libs.apollo.graphql.coroutines.support)
    implementation(libs.apollo.graphql.runtime)
}
