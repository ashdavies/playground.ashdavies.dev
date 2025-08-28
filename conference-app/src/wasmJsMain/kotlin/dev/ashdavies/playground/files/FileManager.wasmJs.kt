package dev.ashdavies.playground.files

internal actual fun FileManager(): FileManager = object : FileManager {
    override fun readByteArray(path: Path) = byteArrayOf()
    override fun delete(path: Path) = Unit
}
