import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class DependencyGroup(
    val group: String? = null,
    val version: String? = null
) {

    fun artifact(
        group: String? = this.group,
        version: String? = this.version
    ): ReadOnlyProperty<DependencyGroup, String> = DependencyGroupImpl(
        version = version,
        group = group
    )
}

private class DependencyGroupImpl(
    private val group: String?,
    private val version: String?
) : ReadOnlyProperty<DependencyGroup, String> {

    override fun getValue(
        thisRef: DependencyGroup,
        property: KProperty<*>
    ): String {
        val artifactName: String = CamelCaseString(property.name).toKebabCase()

        val artifactVersion: String = requireNotNull(version ?: thisRef.version) {
            "Version declaration for dependency with name '$artifactName' not found"
        }

        val artifactGroup: String = requireNotNull(group ?: thisRef.group) {
            "Group declaration for dependency with name '$artifactName' not found"
        }

        return "$artifactGroup:$artifactName:$artifactVersion"
    }
}
