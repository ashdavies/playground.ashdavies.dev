package io.ashdavies.yaml

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

private const val TOKEN_COLON = ":"
private const val TOKEN_COMMENT = "#"
private const val TOKEN_DOCUMENT = "---"
private const val TOKEN_QUOTE = "\""

@ExperimentalSerializationApi
public sealed class Yaml(configuration: YamlConfig) : StringFormat {

    override val serializersModule: SerializersModule = configuration.serializersModule

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        throw UnsupportedOperationException("Yaml serialization is not supported")
    }

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        return deserializer.deserialize(YamlDecoder(serializersModule, string))
    }

    public companion object Default : Yaml(YamlConfig())
}

@ExperimentalSerializationApi
public data class YamlConfig(
    val serializersModule: SerializersModule = EmptySerializersModule()
)

@ExperimentalSerializationApi
internal class YamlDecoder(
    override val serializersModule: SerializersModule,
    string: String,
) : AbstractDecoder() {

    private val lineSequence: Iterator<String> = string
        .lineSequence()
        .iterator()

    private lateinit var currentToken: String

    override fun decodeElementIndex(
        descriptor: SerialDescriptor,
    ): Int = when (lineSequence.hasNext()) {
        true -> descriptor.getElementIndex(decodeKey())
        false -> CompositeDecoder.DECODE_DONE
    }

    override fun decodeString(): String = takeString(true)
        .substringBefore(TOKEN_COMMENT)
        .split(TOKEN_COLON, limit = 2)[1]
        .trim()
        .removeSurrounding(TOKEN_QUOTE)

    override fun decodeBoolean(): Boolean = decodeString().toBoolean()

    private fun decodeKey(): String = takeString(false)
        .split(TOKEN_COLON, limit = 2)
        .first()
        .trim()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        nextToken()
        return this
    }

    private fun takeString(advance: Boolean = true): String {
        val currentToken: String = currentToken
        if (advance) nextToken()
        return currentToken
    }

    private fun nextToken() {
        do {
            currentToken = lineSequence.next()
        } while (!hasValidToken() && lineSequence.hasNext())
    }

    private fun hasValidToken(): Boolean {
        return currentToken != TOKEN_DOCUMENT && currentToken.isNotEmpty()
    }
}
