import com.android.build.api.dsl.VariantDimension

plugins {
    id("io.ashdavies.library")
}

android {
    namespace = "io.ashdavies.local.remote"

    defaultConfig {
        val clientName by SystemProperty(VariantDimension::buildConfigField) { "Ktor/${libs.versions.ktor.get()}" }
    }
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.localStorage)
        implementation(libs.bundles.ktor.client)
    }
}
