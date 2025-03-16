package io.ashdavies.lanyard.gallery

import io.ktor.util.cio.readChannel
import io.ktor.utils.io.ByteReadChannel

internal actual fun File.readChannel(): ByteReadChannel = readChannel()
