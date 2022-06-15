package io.ashdavies.playground.cloud

import com.google.cloud.functions.HttpRequest
import kotlin.properties.ReadOnlyProperty

public fun HttpRequest.firstQueryParameterAsString(supplier: (String?) -> String = { requireNotNull(it) }): ReadOnlyProperty<Any?, String> =
    firstQueryParameter(supplier)

public fun <T> HttpRequest.firstQueryParameter(supplier: (String?) -> T) =
    ReadOnlyProperty<Any?, T> { _, property ->
        getFirstQueryParameter(property.name)
            .map { supplier(it) }
            .orElseGet { supplier(null) }
    }
