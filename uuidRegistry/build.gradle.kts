// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.kotlin.multiplatform)
    id(libs.plugins.sqldelight)

    alias(libs.plugins.serialization)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":sqlDriver"))

                implementation(libs.sqlDelight.coroutinesExtensions)
                implementation(libs.sqlDelight.runtime)
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.serializationJson)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.coroutinesJdk)
            }
        }
    }
}

sqldelight {
    database("UuidRegistry") {
        packageName = "io.ashdavies.notion"
        dialect = "mysql"
    }
}
