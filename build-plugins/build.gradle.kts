plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

<<<<<<< Updated upstream
    plugin(libs.plugins.android.library)
    plugin(libs.plugins.apollo.graphql)
    plugin(libs.plugins.kotlin.compose)
    plugin(libs.plugins.kotlin.multiplatform)
    plugin(libs.plugins.kotlin.serialization)
    plugin(libs.plugins.johnrengelman.shadow)
    plugin(libs.plugins.squareup.sqldelight)
=======
    fun plugin(provider: Provider<PluginDependency>) = with(provider.get()) {
        "$pluginId:$pluginId.gradle.plugin:$version"
    }

    with(libs.plugins) {
        implementation(plugin(android.library))
        implementation(plugin(apollo.graphql))
        implementation(plugin(kotlin.compose))
        implementation(plugin(kotlin.multiplatform))
        implementation(plugin(kotlin.serialization))
        implementation(plugin(johnrengelman.shadow))
        implementation(plugin(squareup.sqldelight))
    }
>>>>>>> Stashed changes
}

fun DependencyHandler.plugin(provider: Provider<PluginDependency>): Dependency? {
    return with(provider.get()) { implementation("$pluginId:$pluginId.gradle.plugin:$version") }
}
