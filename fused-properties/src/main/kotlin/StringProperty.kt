import org.gradle.api.Project
import org.gradle.api.internal.provider.Providers
import org.gradle.api.provider.Provider
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

public fun interface ReadOnlyDelegateProvider<T> : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>>

private fun Project.stringPropertyProvider(propertyName: String): Provider<String> {
    val localPropertiesProvider = rootProject.cachedLocalPropertiesProvider()
    val startPropertiesProvider = startParameterProvider()

    val propertyNameParts = propertyName.split(Regex("(?=[A-Z])"))
    val gradlePropertyName = propertyNameParts.joinToString(".") { it.lowercase() }
    val envPropertyName = propertyNameParts.joinToString("_") { it.uppercase() }

    return startPropertiesProvider.mapOrNull { it[gradlePropertyName] }
        .orElse(localPropertiesProvider.map { it.getProperty(gradlePropertyName) })
        .orElse(providers.gradleProperty(gradlePropertyName))
        .orElse(providers.environmentVariable(envPropertyName))
}

public fun Project.stringProperty(propertyName: String): String {
    return stringPropertyProvider(propertyName).get()
}

public fun Project.stringProperty(block: (String) -> Unit = { }): ReadOnlyDelegateProvider<String> {
    return ReadOnlyDelegateProvider { _, property ->
        val value = stringProperty(property.name).also(block)
        ReadOnlyProperty { _, _ -> value }
    }
}

private fun <S: Any, T> Provider<S>.mapOrNull(block: (S) -> T?): Provider<T> {
    return flatMap { Providers.ofNullable(block(it)) }
}
