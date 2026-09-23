plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    fun plugin(provider: Provider<PluginDependency>) = provider.map {
        "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
    }

    with(libs.plugins) {
        compileOnly(plugin(kotlin.multiplatform))
    }
}

gradlePlugin {
    plugins {
        register("generatedFixturesPlugin") {
            implementationClass = "GeneratedFixturesPlugin"
            id = "dev.ashdavies.fixtures"
        }
    }
}

kotlin {
    explicitApi()
}
