package dev.ashdavies.asg

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
public data class AsgConference(
    public val name: String,
    public val website: String,
    public val location: String,
    public val dateStart: LocalDate,
    public val dateEnd: LocalDate,
    public val online: Boolean? = null,
    public val status: String? = null,
    public val cfp: Cfp? = null,
) {

    @Serializable
    public data class Cfp(
        public val start: String,
        public val end: String,
        public val site: String?,
    )
}
