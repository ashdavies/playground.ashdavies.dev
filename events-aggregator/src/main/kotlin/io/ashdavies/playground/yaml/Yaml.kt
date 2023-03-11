package io.ashdavies.playground.yaml

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
public sealed class Yaml(private val configuration: YamlConfig) : StringFormat {

    override val serializersModule: SerializersModule
        get() = configuration.serializersModule

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        throw UnsupportedOperationException("Yaml serialization is not supported")

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        deserializer.deserialize(YamlDecoder(serializersModule, string))

    public companion object Default : Yaml(YamlConfig())
}
