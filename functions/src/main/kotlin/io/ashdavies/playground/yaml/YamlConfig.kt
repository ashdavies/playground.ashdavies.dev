package io.ashdavies.playground.yaml

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
internal data class YamlConfig(
    val serializersModule: SerializersModule = EmptySerializersModule
)