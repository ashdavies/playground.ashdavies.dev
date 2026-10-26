package dev.ashdavies.playground.event

public val Event.thumbnailImageSeed: Long
    get() = "$name$location".hashCode().toLong()
