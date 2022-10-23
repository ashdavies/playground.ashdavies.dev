plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(project(":app-check:app-check-compose"))
    implementation(project(":app-check:app-check-sdk"))
}
