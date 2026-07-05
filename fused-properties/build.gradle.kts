plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("fusedPropertiesPlugin") {
            implementationClass = "FusedPropertiesPlugin"
            id = "dev.ashdavies.properties"
        }
    }
}

kotlin {
    explicitApi()
}
