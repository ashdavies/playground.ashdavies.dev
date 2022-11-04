plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    api(projects.appCheck.appCheckSdk)

    implementation(projects.cloudFunctions)
    implementation(projects.localRemote)
}
