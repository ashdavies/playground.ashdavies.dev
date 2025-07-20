package io.ashdavies.tally.files

public expect fun Path(path: String): Path

public expect class Path {
    public val name: String
}
