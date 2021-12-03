import org.gradle.api.NamedDomainObjectContainer

fun NamedDomainObjectContainer<*>.create(vararg names: String) {
    names.forEach { create(it) }
}
