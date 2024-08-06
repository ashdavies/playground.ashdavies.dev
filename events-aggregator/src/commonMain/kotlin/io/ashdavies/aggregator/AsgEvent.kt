package io.ashdavies.aggregator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AsgEvent(
    @SerialName("name") public val name: String,
    @SerialName("website") public val website: String,
    @SerialName("location") public val location: String,
    @SerialName("imageUrl") public val imageUrl: String? = null,
    @SerialName("online") public val online: Boolean? = false,
    @SerialName("status") public val status: String? = null,
    @SerialName("date_start") public val dateStart: String,
    @SerialName("date_end") public val dateEnd: String,
    @SerialName("cfp") public val cfp: Cfp? = null,
) {

    @Serializable
    public data class Cfp(
        @SerialName("start") public val start: String,
        @SerialName("end") public val end: String,
        @SerialName("site") public val site: String? = null,
    )
}
