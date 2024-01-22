import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
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
    override fun obtain(): Properties? = propertiesOrNull(parameters.value)
    interface Parameters : ValueSourceParameters {
        val value: RegularFileProperty
    }
}

private fun propertiesOrNull(property: RegularFileProperty): Properties? {
    val properties = property.asFile.orNull ?: return null
    if (!properties.exists()) return null

    return Properties().apply {
        properties.inputStream().use(::load)
    }
}
