plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(projects.appCheck.appCheckCompose)
    implementation(projects.appCheck.appCheckSdk)
    implementation(projects.cloudFunctions)
    implementation(projects.localRemote)

    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.google.cloud.javaFunctionInvoker)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
}
