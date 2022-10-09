package io.ashdavies.playground.cloud

import com.google.cloud.functions.HttpRequest
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public operator fun <T> HttpRequest.invoke(supplier: (String?) -> T) = ReadOnlyProperty<Any?, T> { _, property ->
    getFirstQueryParameter(property.name, supplier)
}

public operator fun HttpRequest.getValue(thisRef: Any?, property: KProperty<*>): String {
    return getFirstQueryParameter(property.name) { requireNotNull(it) }
}

private fun <T> HttpRequest.getFirstQueryParameter(name: String, supplier: (String?) -> T): T {
    return getFirstQueryParameter(name)
        .map { supplier(it) }
        .orElseGet { supplier(null) }
}
