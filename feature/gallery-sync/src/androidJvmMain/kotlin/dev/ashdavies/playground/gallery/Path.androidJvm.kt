package dev.ashdavies.playground.gallery

public actual fun Path(path: String): Path = kotlinx.io.files.Path(path)

public actual typealias Path = kotlinx.io.files.Path
