package dev.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class ApiConference(
    public val id: String,
    public val name: String,
    public val website: String,
    public val location: String,
    public val dateStart: String,
    public val dateEnd: String,
    public val imageUrl: String?,
    public val status: String?,
    public val online: Boolean?,
    public val cfp: Cfp?,
) {

    @Serializable
    public data class Cfp(
        public val start: String,
        public val end: String,
        public val site: String?,
    )
}
