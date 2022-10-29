@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage") // https://youtrack.jetbrains.com/issue/KTIJ-19369

plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    implementation(libs.ktor.http)
}
