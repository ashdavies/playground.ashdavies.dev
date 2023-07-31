plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    api(projects.appCheck.appCheckCommon)
    api(libs.ktor.client.core)

    implementation(projects.httpClient)

    implementation(libs.auth.java.jwt)
    implementation(libs.auth.jwks.rsa)
    implementation(libs.google.guava.jre)

    with(libs.ktor.client) {
        implementation(auth)
        implementation(content.negotiation)
        implementation(core)
        implementation(json)
        implementation(logging)
        implementation(okhttp3)
    }

    implementation(libs.slf4j.simple)
}
