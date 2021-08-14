package io.ashdavies.playground.google

import com.google.cloud.functions.HttpRequest
import kotlin.properties.ReadOnlyProperty

fun HttpRequest.firstStringQueryParameterOrDefault(supplier: () -> String) =
    firstQueryParameter { it ?: supplier() }

fun HttpRequest.firstIntQueryParameterOrDefault(supplier: () -> Int) =
    firstQueryParameter { it?.toIntOrNull() ?: supplier() }

private fun <T : Any> HttpRequest.firstQueryParameter(supplier: (String?) -> T) =
    ReadOnlyProperty<Any?, T> { _, property ->
        getFirstQueryParameter(property.name)
            .map { supplier(it) }
            .orElseGet { supplier(null) }
    }