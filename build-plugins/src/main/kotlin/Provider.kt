import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.util.Properties

internal abstract class MapProperties : ValueSource<Map<String, String>, MapProperties.Parameters> {
    override fun obtain(): Map<String, String>? = parameters.value.getOrElse(emptyMap())
    interface Parameters : ValueSourceParameters {
        val value: MapProperty<String, String>
    }
}

internal abstract class LocalProperties : ValueSource<Properties, LocalProperties.Parameters> {
    override fun obtain(): Properties? = Properties(parameters.value)
    interface Parameters : ValueSourceParameters {
        val value: RegularFileProperty
    }
}

private fun Project.startParameterProperties(key: String): Provider<String> {
    return providers.of(MapProperties::class.java) {
        parameters.value.set(gradle.startParameter.projectProperties)
        parameters.value.disallowChanges()
    }.flatMap { provider { it[key] } }
}

internal fun Project.localProperty(path: String, propertyName: String): Provider<String> {
    return providers.of(LocalProperties::class.java) {
        parameters.value.set(project.layout.projectDirectory.file(path))
        parameters.value.disallowChanges()
    }.map { it.getProperty(propertyName) }
}

private fun Properties(property: RegularFileProperty): Properties? {
    val properties = property.asFile.orNull ?: return null
    if (!properties.exists()) return null

    return Properties().apply {
        properties.inputStream().use(::load)
    }
}

internal fun Project.fusedGradleProperty(propertyName: String): Provider<String> = rootProject
    .startParameterProperties(propertyName)
    .orElse(localProperty("local.properties", propertyName))
    .orElse(localProperty("gradle.properties", propertyName))
    .orElse(rootProject.localProperty("gradle.properties", propertyName))
    .orElse(providers.gradleProperty(propertyName))

public fun Project.stringProperty(propertyName: String): String {
    return fusedGradleProperty(propertyName).get()
}

public fun Project.stringPropertyOrNull(propertyName: String): String? {
    return fusedGradleProperty(propertyName).orNull
}
