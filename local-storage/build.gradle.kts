// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    `multiplatform-library`
    `multiplatform-sql`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":compose-local"))
            }
        }
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
        dialect = "sqlite:3.25"
    }
}
