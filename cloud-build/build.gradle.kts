plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("cloudBuildPlugin") {
            implementationClass = "CloudBuildPlugin"
            id = "dev.ashdavies.cloud.build"
        }
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.jib.core)
}

kotlin {
    explicitApi()
}
