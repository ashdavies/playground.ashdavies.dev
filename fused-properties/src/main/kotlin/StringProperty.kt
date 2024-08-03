import org.gradle.api.Project
import org.gradle.api.internal.provider.Providers
import org.gradle.api.provider.Provider
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

public fun interface ReadOnlyDelegateProvider<T> : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>>

private fun Project.stringPropertyProvider(propertyName: String): Provider<String> {
    val rootPropertiesProvider = rootProject.cachedLocalPropertiesProvider()
    val localPropertiesProvider = cachedLocalPropertiesProvider()
    val startPropertiesProvider = startParameterProvider()

    val propertyNameParts = propertyName.split(Regex("(?=[A-Z])"))
    val gradlePropertyName = propertyNameParts.joinToString(".") { it.lowercase() }
    val envPropertyName = propertyNameParts.joinToString("_") { it.uppercase() }

    return startPropertiesProvider.mapOrNull { it[gradlePropertyName] }
        .orElse(localPropertiesProvider.map { it.getProperty(gradlePropertyName) })
        .orElse(rootPropertiesProvider.map { it.getProperty(gradlePropertyName) })
        .orElse(providers.gradleProperty(gradlePropertyName))
        .orElse(providers.environmentVariable(envPropertyName))
}

public fun Project.booleanProperty(block: (Boolean) -> Unit = { }): ReadOnlyDelegateProvider<Boolean> {
    return readOnlyDelegateProvider { it.get().toBoolean().also(block) }
}

public fun Project.booleanPropertyOrNull(block: (Boolean?) -> Unit = { }): ReadOnlyDelegateProvider<Boolean> {
    return readOnlyDelegateProvider { it.orNull.toBoolean().also(block) }
}

public fun Project.stringProperty(block: (String) -> Unit = { }): ReadOnlyDelegateProvider<String> {
    return readOnlyDelegateProvider { it.get().also(block) }
}

public fun Project.stringPropertyOrNull(block: (String?) -> Unit = { }): ReadOnlyDelegateProvider<String?> {
    return readOnlyDelegateProvider { it.orNull.also(block) }
}

private fun <T> Project.readOnlyDelegateProvider(transform: (Provider<String>) -> T): ReadOnlyDelegateProvider<T> {
    return ReadOnlyDelegateProvider { _, property ->
        val value = transform(stringPropertyProvider(property.name))
        ReadOnlyProperty { _, _ -> value }
    }
}

private fun <S : Any, T> Provider<S>.mapOrNull(block: (S) -> T?): Provider<T> {
    return flatMap { Providers.ofNullable(block(it)) }
}
