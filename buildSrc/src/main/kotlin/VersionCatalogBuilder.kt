@file:Suppress("UnstableApiUsage")

import VersionCatalogGroup.Unversioned
import VersionCatalogGroup.Versioned
import org.gradle.api.initialization.dsl.VersionCatalogBuilder

fun VersionCatalogBuilder.group(group: String, block: Unversioned.() -> Unit) = when (this) {
    is VersionCatalogGroup -> Unversioned(this, "$group:$group")
    else -> Unversioned(this, group)
}.apply(block)

fun VersionCatalogBuilder.group(group: String, version: String, block: Versioned.() -> Unit) =
    Versioned(this, group, version).apply(block)

fun VersionCatalogBuilder.androidx(block: Unversioned.() -> Unit) =
    group("androidx", block)

fun VersionCatalogBuilder.com(block: Unversioned.() -> Unit) =
    group("com", block)
