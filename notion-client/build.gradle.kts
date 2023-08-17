plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.sql")
}

android {
    namespace = "io.ashdavies.notion"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigString("NOTION_CLIENT_ID") { stringPropertyOrNull("notion.client.id") }
        buildConfigString("NOTION_CLIENT_SECRET") { stringPropertyOrNull("notion.client.secret") }
    }
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)
        implementation(projects.sqlDriver)

        with(libs.ktor.client) {
            implementation(auth)
            implementation(content.negotiation)
            implementation(logging)
        }

        implementation(libs.ktor.serialization.json)
        implementation(libs.ktor.serialization.kotlinx)

        with(libs.ktor.server) {
            implementation(auth)
            implementation(call.logging)
            implementation(cio)
            implementation(compression)
            implementation(conditional.headers)
            implementation(content.negotiation)
            implementation(core)
            implementation(default.headers)
            implementation(request.validation)
        }

        implementation(libs.slf4j.simple)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set("io.ashdavies.notion")
            dependency(projects.localStorage)
        }
    }
}
