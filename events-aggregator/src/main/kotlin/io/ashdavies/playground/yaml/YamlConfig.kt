package io.ashdavies.playground.yaml

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
internal data class YamlConfig(val serializersModule: SerializersModule = EmptySerializersModule)
