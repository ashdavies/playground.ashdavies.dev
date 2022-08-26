package io.ashdavies.playground.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Property

internal inline fun <reified T> ExtensionContainer.create(name: String, vararg args: Any?) {
    create(name, T::class.java, *args)
}

internal inline fun <reified T> ExtensionContainer.getByType(): T {
    return getByType(T::class.java)
}

internal inline fun <reified T> ObjectFactory.property(): Property<T> {
    return property(T::class.java)
}
