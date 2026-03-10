plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    fun plugin(provider: Provider<PluginDependency>) = provider.map {
        "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
    }

    with(libs.plugins) {
        compileOnly(plugin(android.library))
        compileOnly(plugin(compose.compiler))
        compileOnly(plugin(detekt))
        compileOnly(plugin(jetbrains.compose))
        compileOnly(plugin(kotlin.multiplatform))
        compileOnly(plugin(kotlin.serialization))
        compileOnly(plugin(ktlint))
    }
}

kotlin {
    explicitApi()
}

gradlePlugin {
    plugins {
        register("androidConventionPlugin") {
            implementationClass = "AndroidConventionPlugin"
            id = "dev.ashdavies.android"
        }

        register("composeConventionPlugin") {
            implementationClass = "ComposeConventionPlugin"
            id = "dev.ashdavies.compose"
        }

        register("jvmConventionPlugin") {
            implementationClass = "JvmConventionPlugin"
            id = "dev.ashdavies.jvm"
        }

        register("kotlinConventionPlugin") {
            implementationClass = "KotlinConventionPlugin"
            id = "dev.ashdavies.kotlin"
        }

        register("parcelableConventionPlugin") {
            implementationClass = "ParcelableConventionPlugin"
            id = "dev.ashdavies.parcelable"
        }

        register("wasmConventionPlugin") {
            implementationClass = "WasmConventionPlugin"
            id = "dev.ashdavies.wasm"
        }
    }
}
