import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private const val GROUP = "group"
private const val NAME = "name"
private const val VERSION = "version"

abstract class DependencyGroup(
    group: String? = null,
    version: String? = null
) : Map<String, String?> by mapOf(
    GROUP to group,
    VERSION to version
)

fun dependency(
    name: String? = null,
    version: String? = null
): ReadOnlyProperty<DependencyGroup, Map<String, String?>> {
    return DependencyGroupArtifact(name, version)
}

private class DependencyGroupArtifact(
    private val name: String? = null,
    private val version: String? = null
): ReadOnlyProperty<DependencyGroup, Map<String, String?>> {

    override fun getValue(
        thisRef: DependencyGroup,
        property: KProperty<*>
    ): Map<String, String?> = mapOf(
        GROUP to thisRef[GROUP],
        NAME to (thisRef[NAME] ?: name ?: property.name),
        VERSION to (thisRef[VERSION] ?: version)
    )
}
