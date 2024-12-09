import org.gradle.api.Project
import org.gradle.api.internal.provider.Providers
import org.gradle.api.provider.Provider
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

public fun interface ReadOnlyDelegateProvider<T> : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>>

private interface PropertyDefinition {
    val gradlePropertyName: String
    val envPropertyName: String
}

private fun PropertyDefinition(propertyName: String) = object : PropertyDefinition {
    private val propertyNameParts = propertyName.split(Regex("(?=[A-Z])"))
    override val gradlePropertyName = propertyNameParts.joinToString(".") { it.lowercase() }
    override val envPropertyName = propertyNameParts.joinToString("_") { it.uppercase() }
}

private fun Project.stringPropertyProvider(definition: PropertyDefinition): Provider<String> {
    val rootPropertiesProvider = rootProject.cachedLocalPropertiesProvider()
    val localPropertiesProvider = cachedLocalPropertiesProvider()
    val startPropertiesProvider = startParameterProvider()

    return startPropertiesProvider.mapOrNull { it[definition.gradlePropertyName] }
        .orElse(localPropertiesProvider.map { it.getProperty(definition.gradlePropertyName) })
        .orElse(rootPropertiesProvider.map { it.getProperty(definition.gradlePropertyName) })
        .orElse(providers.gradleProperty(definition.gradlePropertyName))
        .orElse(providers.environmentVariable(definition.envPropertyName))
}

public fun Project.booleanProperty(block: (String, Boolean?) -> Unit): ReadOnlyDelegateProvider<Boolean?> {
    return readOnlyDelegateProvider { provider, tag -> provider.get().toBoolean().also { block(tag, it) } }
}

public fun Project.stringProperty(block: (String, String?) -> Unit): ReadOnlyDelegateProvider<String?> {
    return readOnlyDelegateProvider { provider, tag -> provider.orNull.also { block(tag, it) } }
}

private fun <T> Project.readOnlyDelegateProvider(
    transform: (provider: Provider<String>, tag: String) -> T,
): ReadOnlyDelegateProvider<T> = ReadOnlyDelegateProvider { _, property ->
    val definition = PropertyDefinition(property.name)

    val value = transform(
        stringPropertyProvider(definition),
        definition.envPropertyName,
    )

    ReadOnlyProperty { _, _ -> value }
}

private fun <S : Any, T> Provider<S>.mapOrNull(block: (S) -> T?): Provider<T> {
    return flatMap { Providers.ofNullable(block(it)) }
}
