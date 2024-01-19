plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(compose.runtime)

    implementation(libs.auth.java.jwt)
    implementation(libs.auth.jwks.rsa)
    implementation(libs.google.auth.http)
    implementation(libs.google.firebase.admin)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.ktor.client.core)

    runtimeOnly(libs.google.guava.jre)
    runtimeOnly(libs.slf4j.simple)
}
