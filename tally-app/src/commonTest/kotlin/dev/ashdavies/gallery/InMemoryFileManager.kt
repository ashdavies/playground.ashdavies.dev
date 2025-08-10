package dev.ashdavies.gallery

import dev.ashdavies.tally.files.FileManager
import dev.ashdavies.tally.files.Path
import kotlinx.io.files.FileNotFoundException

private val EMPTY_BYTE_ARRAY = ByteArray(0)

internal class InMemoryFileManager(initialValue: Map<Path, ByteArray> = emptyMap()) : FileManager {

    private val storage = initialValue.toMutableMap()

    constructor(vararg paths: Path) : this(paths.associateWith { EMPTY_BYTE_ARRAY })

    override fun readByteArray(path: Path): ByteArray {
        return storage[path] ?: throw FileNotFoundException("$path")
    }

    override fun delete(path: Path) {
        storage.remove(path)
    }
}
