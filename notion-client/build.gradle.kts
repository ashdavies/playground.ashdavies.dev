import com.android.build.api.dsl.VariantDimension

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
        buildConfigString("NOTION_CLIENT_ID")
        buildConfigString("NOTION_CLIENT_SECRET")
    }
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)
        implementation(projects.sqlDriver)

        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.ktor.serialization)
        implementation(libs.bundles.ktor.server)

        implementation(libs.ktor.client.auth)
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        dependency(projects.localStorage.dependencyProject)
        packageName = "io.ashdavies.notion"
    }
}

fun VariantDimension.buildConfigString(name: String, value: String = System.getenv(name)) {
    buildConfigField("String", name, "\"$value\"")
}
