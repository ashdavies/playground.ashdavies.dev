import com.android.build.api.dsl.VariantDimension

plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.local.remote"

    defaultConfig {
        val clientName by SystemProperty(VariantDimension::buildConfigField) { "Ktor/${libs.versions.ktor.get()}" }
    }
}

kotlin {
    commonMain.dependencies {
        implementation(projects.localStorage)
        implementation(libs.bundles.ktor.client)
    }
}
