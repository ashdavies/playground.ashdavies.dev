plugins {
    `multiplatform-library`
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            implementation(project(":local-storage"))

            implementation(libs.bundles.ktor.client)
        }
    }
}
