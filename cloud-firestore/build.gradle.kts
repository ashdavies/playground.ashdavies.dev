plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

buildConfig {
    val dryRun by booleanPropertyOrNull() { value ->
        buildConfigField("DRY_RUN", value)
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }

        jvmMain.dependencies {
            implementation(dependencies.platform(libs.google.cloud.bom))
            implementation(libs.google.cloud.firestore)
        }
    }
}
