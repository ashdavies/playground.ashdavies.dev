plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(projects.appCheck.appCheckCompose)
    implementation(projects.appCheck.appCheckSdk)
    implementation(projects.cloudFunctions)
    implementation(projects.localStorage)

    testImplementation(testFixtures(projects.cloudFunctions))
}
