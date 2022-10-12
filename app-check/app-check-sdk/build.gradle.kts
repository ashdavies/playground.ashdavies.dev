plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    api(libs.auth.java.jwt)
    api(libs.ktor.client.auth)
    api(libs.ktor.client.core)

    implementation(libs.auth.jwks.rsa)
    implementation(libs.bundles.ktor.client)
    implementation(libs.ktor.client.auth)
}
