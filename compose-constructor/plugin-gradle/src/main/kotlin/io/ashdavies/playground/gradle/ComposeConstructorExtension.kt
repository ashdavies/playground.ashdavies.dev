package io.ashdavies.playground.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

internal class ComposeConstructorExtension(factory: ObjectFactory) {
    val enabled: Property<Boolean> = factory.property()
}
