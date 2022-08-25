package io.ashdavies.playground.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

private val Project.extension: ComposeConstructorExtension
    get() = extensions.getByType()

internal class ComposeConstructorPlugin : KotlinCompilerPluginSupportPlugin {

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return with(kotlinCompilation.target.project) {
            provider { emptyList() }
        }
    }

    override fun getCompilerPluginId(): String {
        return "io.ashdavies.playground.compose-constructor"
    }

    override fun getPluginArtifact() = SubpluginArtifact(
        groupId = "io.ashdavies.playground",
        artifactId = "compose-constructor",
        version = "1.0.0-SNAPSHOT",
    )

    override fun getPluginArtifactForNative() = SubpluginArtifact(
        artifactId = "compose-constructor-native",
        groupId = "io.ashdavies.playground",
        version = "1.0.0-SNAPSHOT",
    )

    override fun apply(target: Project) = with(target) {
        extensions.create<ComposeConstructorExtension>("compose-constructor")
    }
}
