package io.ashdavies.http

import java.lang.System
import java.lang.reflect.Field
import java.lang.reflect.Modifier

private const val APPLICATION_ID = "io.ashdavies.playground"

private val Field.isStatic: Boolean get() = Modifier.isStatic(modifiers)

private val Field.isString: Boolean get() = type.isAssignableFrom(String::class.java)

private val buildConfigCache = mutableMapOf<String, Map<String, String>>()

public actual object System : Environment {

    override val properties: Map<Any, Any> = System.getProperties()

    @Suppress("MemberVisibilityCanBePrivate")
    public var buildConfig: String = "$APPLICATION_ID.BuildConfig"

    override val env: Map<String, String>
        get() = buildConfigCache.getOrPut(buildConfig) {
            loadBuildConfig(buildConfig)
        }
}

private fun loadBuildConfig(className: String): Map<String, String> = buildMap {
    Class.forName(className).declaredFields.forEach {
        if (it.isStatic && it.isString) put(it.name, it.get(null) as String)
    }
}
