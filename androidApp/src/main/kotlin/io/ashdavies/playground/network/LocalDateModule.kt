package io.ashdavies.playground.network

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDate.Companion.parse

internal class LocalDateModule : SimpleModule("LocalDateModule") {

    init {
        addDeserializer(LocalDate::class.java, LocalDateDeserializer())
    }

    private class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {

        override fun deserialize(
            parser: JsonParser,
            context: DeserializationContext
        ): LocalDate = parse(parser.valueAsString)
    }
}
