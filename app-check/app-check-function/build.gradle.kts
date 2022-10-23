plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(project(":app-check:app-check-compose"))
    implementation(project(":app-check:app-check-sdk"))

    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.google.cloud.javaFunctionInvoker)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
}
