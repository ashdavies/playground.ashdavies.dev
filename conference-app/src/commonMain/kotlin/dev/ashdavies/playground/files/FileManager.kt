package dev.ashdavies.playground.files

internal expect fun FileManager(): FileManager

internal interface FileManager {
    fun readByteArray(path: Path): ByteArray
    fun delete(path: Path)
}
