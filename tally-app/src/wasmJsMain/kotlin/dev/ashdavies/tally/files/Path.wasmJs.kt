package dev.ashdavies.tally.files

public actual fun Path(path: String): Path = Path(Details(path.substringAfterLast('/')))

public actual class Path internal constructor(details: Details) {
    public actual val name: String = details.name
}

internal data class Details(val name: String)
