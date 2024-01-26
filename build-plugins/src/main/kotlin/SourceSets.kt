import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.NamedDomainObjectCollectionDelegateProvider
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import kotlin.properties.ReadOnlyProperty

public val KotlinMultiplatformExtension.androidMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidDebug: SourceSetInvoker by InvokerDelegate()

public val KotlinMultiplatformExtension.commonMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.commonTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.jvmIntegrationTest: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.jvmMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.jvmTest: KotlinSourceSet by SourceSetDelegate()

public operator fun KotlinSourceSet.invoke(action: KotlinSourceSet.() -> Unit): Unit = action()

internal typealias SourceSetDelegate = ReadOnlyProperty<KotlinSourceSetContainer, KotlinSourceSet>
internal typealias SourceInvokerDelegate = ReadOnlyProperty<KotlinMultiplatformExtension, SourceSetInvoker>
internal typealias SourceSetInvoker = (KotlinSourceSet.() -> Unit) -> Unit

internal fun SourceSetDelegate(): SourceSetDelegate = SourceSetDelegate { thisRef, property ->
    thisRef.sourceSets[property.name]
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun InvokerDelegate(): SourceInvokerDelegate = SourceInvokerDelegate { thisRef, property ->
    { thisRef.apply { sourceSets.invokeWhenCreated(property.name, it) } }
}

public fun NamedDomainObjectCollection<KotlinSourceSet>.dependencies(
    configure: KotlinDependencyHandler.() -> Unit,
): NamedDomainObjectCollectionDelegateProvider<KotlinSourceSet> = getting {
    dependencies(configure)
}

public fun SourceSetInvoker.dependencies(
    configure: KotlinDependencyHandler.() -> Unit,
): Unit = invoke { dependencies(configure) }

public fun KotlinDependencyHandler.implementation(
    provider: Provider<MinimalExternalModuleDependency>,
    configure: ExternalModuleDependency.() -> Unit,
): ExternalModuleDependency = implementation(
    dependencyNotation = "${provider.get()}",
    configure = configure,
)
