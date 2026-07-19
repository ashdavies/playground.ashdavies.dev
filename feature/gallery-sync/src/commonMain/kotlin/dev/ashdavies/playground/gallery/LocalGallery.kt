package dev.ashdavies.playground.gallery

import dev.ashdavies.sql.Suspended
import kotlin.jvm.JvmInline

@JvmInline
public value class LocalGallery(public val enabled: Suspended<Boolean>)
