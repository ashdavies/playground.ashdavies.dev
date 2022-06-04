plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":local-storage"))
    implementation(libs.coroutine.dispatcher.core)

    with(libs.jetbrains.kotlinx) {
        implementation(coroutines.core)
        implementation(datetime)

        with(serialization) {
            implementation(core)
            implementation(json)
            implementation(properties)
        }
    }

    implementation(libs.google.cloud.functionsFrameworkApi)
    implementation(libs.google.firebase.admin)
}
