plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    commonMain.dependencies {
        implementation(libs.google.ksp.api)
        implementation(libs.squareup.kotlinpoet.ksp)
    }
}
