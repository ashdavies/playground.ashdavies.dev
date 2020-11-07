package io.ashdavies.playground.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class StringValueSerializer : KSerializer<String> {

    private val serializer: KSerializer<FirestoreString> =
        FirestoreString.serializer()

    override val descriptor: SerialDescriptor =
        serializer.descriptor

    override fun serialize(encoder: Encoder, value: String) =
        encoder.encodeSerializableValue(serializer, FirestoreString(value))

    override fun deserialize(decoder: Decoder): String =
        decoder
            .decodeSerializableValue(serializer)
            .stringValue
}
