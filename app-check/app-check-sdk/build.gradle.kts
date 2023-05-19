plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    api(projects.appCheck.appCheckCommon)
    api(libs.ktor.client.core)

    implementation(projects.cloudBackend.localRemote)

    implementation(libs.auth.java.jwt)
    implementation(libs.auth.jwks.rsa)
    implementation(libs.bundles.ktor.client)
    implementation(libs.ktor.client.auth)
}
