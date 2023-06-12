import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public fun NamedDomainObjectCollection<KotlinSourceSet>.dependencies(
    configure: KotlinDependencyHandler.() -> Unit,
) = getting { dependencies(configure) }

public fun SourceSetInvoker.dependencies(
    configure: KotlinDependencyHandler.() -> Unit,
) = invoke { dependencies(configure) }

public fun KotlinDependencyHandler.implementation(
    provider: Provider<MinimalExternalModuleDependency>,
    configure: ExternalModuleDependency.() -> Unit,
) = implementation("${provider.get()}", configure)

public inline fun <reified T> ExtensionContainer.withType(
    configure: T.() -> Unit,
) = findByType(T::class.java)?.configure()

public fun <T : ModuleDependency> T.exclude(
    provider: Provider<MinimalExternalModuleDependency>,
): T = exclude(provider.get())

public fun <T : ModuleDependency> T.exclude(
    dependency: MinimalExternalModuleDependency,
): T = exclude(dependency.group, dependency.name)
