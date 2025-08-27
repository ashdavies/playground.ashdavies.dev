package dev.ashdavies.playground.files

import kotlinx.io.files.Path

public actual fun Path(path: String): Path = Path(path)

public actual typealias Path = Path
