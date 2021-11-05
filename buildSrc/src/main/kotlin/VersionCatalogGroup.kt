@file:Suppress("UnstableApiUsage")

import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * TODO
 * - Create inline classes for group and version
 * - Use provider to call alias immediately
 * - Check for dependency collisions
 */
sealed class VersionCatalogGroup(
    delegate: VersionCatalogBuilder,
) : VersionCatalogBuilder by delegate {

    abstract val group: String

    class Versioned(
        delegate: VersionCatalogBuilder,
        override val group: String,
        val version: String,
    ) : VersionCatalogGroup(delegate)

    class Unversioned(
        delegate: VersionCatalogBuilder,
        override val group: String,
    ) : VersionCatalogGroup(delegate)
}

fun VersionCatalogGroup.artifact(name: String, version: String) =
    alias("$group-$name")
        .to(group, name)
        .version(version)

fun VersionCatalogGroup.Versioned.artifact(name: String) =
    artifact(name, version)

