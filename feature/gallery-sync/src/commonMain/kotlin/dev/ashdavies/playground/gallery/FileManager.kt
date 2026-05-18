package dev.ashdavies.playground.gallery

internal expect fun FileManager(): FileManager

internal interface FileManager {
    fun readByteArray(path: Path): ByteArray
    fun delete(path: Path)
}
