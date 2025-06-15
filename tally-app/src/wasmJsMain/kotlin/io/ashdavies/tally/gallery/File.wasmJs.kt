package io.ashdavies.tally.gallery

public actual class File actual constructor(private val path: String) {
    public actual fun getAbsolutePath(): String = path
    public actual fun getName(): String = path.substringAfterLast("/")
    public actual fun length(): Long = 0L
}
