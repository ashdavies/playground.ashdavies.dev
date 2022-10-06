plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    plugin(libs.plugins.android.library)
    plugin(libs.plugins.apollo.graphql)
    plugin(libs.plugins.kotlin.compose)
    plugin(libs.plugins.kotlin.multiplatform)
    plugin(libs.plugins.kotlin.serialization)
    plugin(libs.plugins.johnrengelman.shadow)
    plugin(libs.plugins.squareup.sqldelight)
}

fun DependencyHandler.plugin(provider: Provider<PluginDependency>): Dependency? {
    return with(provider.get()) { implementation("$pluginId:$pluginId.gradle.plugin:$version") }
}
