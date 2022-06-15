plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.tools.build.gradle)
    implementation(libs.apollographql.apollo.gradle.plugin)
    implementation(libs.jetbrains.compose.gradle.plugin)
    implementation(libs.jetbrains.kotlin.gradle.plugin)
    implementation(libs.jetbrains.kotlin.serialization.gradlePlugin)
    implementation(libs.johnRengelman.shadow)
    implementation(libs.sqldelight.kotlin.gradle.plugin)
}
