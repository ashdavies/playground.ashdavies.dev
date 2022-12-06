plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    fun DependencyHandler.plugin(provider: Provider<PluginDependency>) = with(provider.get()) {
        implementation("$pluginId:$pluginId.gradle.plugin:$version")
    }

    with(libs.plugins) {
        plugin(android.library)
        plugin(apollo.graphql)
        plugin(kotlin.compose)
        plugin(kotlin.multiplatform)
        plugin(kotlin.serialization)
        plugin(johnrengelman.shadow)
        plugin(squareup.sqldelight)
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}
