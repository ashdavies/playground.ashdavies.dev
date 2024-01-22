import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.util.Properties

private const val LOCAL_PROPERTIES_KEY = "io.ashdavies.properties.local"
private const val LOCAL_PROPERTIES_PATH = "local.properties"

internal fun Project.startParameterProvider(): Provider<Map<String, String>> {
    return providers.of(MapProperties::class.java) {
        parameters.value.set(gradle.startParameter.projectProperties)
        parameters.value.disallowChanges()
    }
}

private fun Project.localPropertyProvider(path: String): Provider<Properties> {
    return providers.of(LocalProperties::class.java) {
        parameters.value.set(project.layout.projectDirectory.file(path))
        parameters.value.disallowChanges()
    }
}

internal fun Project.cachedLocalPropertiesProvider(): Provider<Properties> {
    return project.getOrCreateExtra(LOCAL_PROPERTIES_KEY) {
        it.localPropertyProvider(LOCAL_PROPERTIES_PATH)
    }
}

private fun <T> Project.getOrCreateExtra(key: String, body: (Project) -> T): T {
    with(project.extensions.extraProperties) {
        if (!has(key)) set(key, body(project))

        @Suppress("UNCHECKED_CAST")
        return get(key) as? T ?: body(project)
    }
}
