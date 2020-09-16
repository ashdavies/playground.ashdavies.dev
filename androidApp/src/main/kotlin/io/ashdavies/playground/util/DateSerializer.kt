package io.ashdavies.playground.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

@ExperimentalSerializationApi
@Serializer(forClass = DateSerializer::class)
object DateSerializer : KSerializer<Date> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("DateSerializer", STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }
}
