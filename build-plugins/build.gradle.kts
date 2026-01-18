plugins {
    `kotlin-dsl`
}

dependencies {
    // Ensure version catalog access in this build
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    fun plugin(provider: Provider<PluginDependency>) = with(provider.get()) {
        "$pluginId:$pluginId.gradle.plugin:$version"
    }

    // Convention plugins apply these plugin IDs at runtime, so they must be on the classpath
    implementation(plugin(libs.plugins.android.library))
    implementation(plugin(libs.plugins.compose.compiler))
    implementation(plugin(libs.plugins.detekt))
    implementation(plugin(libs.plugins.jetbrains.compose))
    implementation(plugin(libs.plugins.kotlin.multiplatform))
    implementation(plugin(libs.plugins.kotlin.serialization))
    implementation(plugin(libs.plugins.ktlint))
}

gradlePlugin {
    plugins {
        register("androidConventionPlugin") {
            id = "dev.ashdavies.android"
            implementationClass = "AndroidConventionPlugin"
        }

        register("composeConventionPlugin") {
            id = "dev.ashdavies.compose"
            implementationClass = "ComposeConventionPlugin"
        }

        register("defaultConventionPlugin") {
            id = "dev.ashdavies.default"
            implementationClass = "DefaultConventionPlugin"
        }

        register("kotlinConventionPlugin") {
            id = "dev.ashdavies.kotlin"
            implementationClass = "KotlinConventionPlugin"
        }

        register("parcelableConventionPlugin") {
            id = "dev.ashdavies.parcelable"
            implementationClass = "ParcelableConventionPlugin"
        }

        register("wasmConventionPlugin") {
            id = "dev.ashdavies.wasm"
            implementationClass = "WasmConventionPlugin"
        }
    }
}

kotlin {
    explicitApi()
}
