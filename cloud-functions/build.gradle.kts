plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":local-storage"))

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)
}
