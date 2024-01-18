import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.apollographql.apollo")
    kotlin("jvm")
}

apollo {
    generateKotlinModels.set(true)
}
