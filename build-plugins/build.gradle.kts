plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    fun plugin(provider: Provider<PluginDependency>) = with(provider.get()) {
        "$pluginId:$pluginId.gradle.plugin:$version"
    }

    with(libs.plugins) {
        implementation(plugin(android.library))
        implementation(plugin(detekt))
        implementation(plugin(kotlin.compose))
        implementation(plugin(kotlin.multiplatform))
        implementation(plugin(kotlin.serialization))
        implementation(plugin(ktlint))
    }
}

kotlin {
    explicitApi()
}
