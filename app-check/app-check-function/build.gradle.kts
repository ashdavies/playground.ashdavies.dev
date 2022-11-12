plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(projects.appCheck.appCheckCompose)
    implementation(projects.appCheck.appCheckSdk)
    implementation(projects.cloudFunctions)
    implementation(projects.localRemote)

    testImplementation(testFixtures(projects.cloudFunctions))
}
