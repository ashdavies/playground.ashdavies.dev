import org.jetbrains.compose.compose

plugins {
    id("io.ashdavies.cloud")

    `java-test-fixtures`
}

dependencies {
    implementation(projects.composeLocals)
    implementation(libs.ktor.http)

    testFixturesImplementation(compose.runtime)
    testFixturesImplementation(libs.bundles.ktor.client)
    testFixturesImplementation(libs.bundles.ktor.serialization)
    testFixturesImplementation(libs.google.cloud.javaFunctionInvoker)
    testFixturesImplementation(libs.jetbrains.kotlinx.coroutines.test)
}
