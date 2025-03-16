package io.ashdavies.lanyard.gallery

public expect class File(path: String) {
    public fun getAbsolutePath(): String
    public fun getName(): String
    public fun length(): Long
}
