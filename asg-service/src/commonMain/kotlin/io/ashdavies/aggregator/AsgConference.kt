package io.ashdavies.aggregator

import kotlinx.serialization.Serializable

@Serializable
public data class AsgConference(
    public val name: String,
    public val website: String,
    public val location: String,
    public val online: Boolean? = false,
    public val status: String? = null,
    public val dateStart: String,
    public val dateEnd: String,
    public val cfp: Cfp? = null,
) {

    @Serializable
    public data class Cfp(
        public val start: String,
        public val end: String,
        public val site: String? = null,
    )
}
