plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    api(libs.auth.java.jwt)
    api(libs.google.firebase.admin)
    api(libs.ktor.client.core)

    implementation(libs.auth.jwks.rsa)
    implementation(libs.google.auth.http)
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.ktor.http)
    implementation(libs.ktor.utils)

    runtimeOnly(libs.google.guava.jre)
    runtimeOnly(libs.slf4j.simple)
}
