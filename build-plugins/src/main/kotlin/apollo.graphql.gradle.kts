@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.apollographql.apollo")
    kotlin("jvm")
}

apollo {
    generateKotlinModels.set(true)
}

dependencies {
    implementation(libs.apollographql.apollo.runtime)
    implementation(libs.apollographql.apollo.coroutines.support)
}
