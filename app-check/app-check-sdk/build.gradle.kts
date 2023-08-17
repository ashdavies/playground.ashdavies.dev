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
    implementation(libs.slf4j.simple)
}
