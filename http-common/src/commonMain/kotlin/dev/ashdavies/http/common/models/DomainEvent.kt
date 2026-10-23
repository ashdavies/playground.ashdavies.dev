package dev.ashdavies.http.common.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
public data class DomainEvent(
    public val uuid: Uuid,
    public val name: String,
    public val website: String,
    public val location: String,
    public val dateStart: LocalDate,
    public val dateEnd: LocalDate,
)
