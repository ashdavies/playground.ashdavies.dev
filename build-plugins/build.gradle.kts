import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        implementation(plugin(compose.compiler))
        implementation(plugin(detekt))
        implementation(plugin(jetbrains.compose))
        implementation(plugin(kotlin.multiplatform))
        implementation(plugin(kotlin.serialization))
        implementation(plugin(ktlint))
    }

    implementation(libs.jib.core)
}

kotlin {
    explicitApi()
}
