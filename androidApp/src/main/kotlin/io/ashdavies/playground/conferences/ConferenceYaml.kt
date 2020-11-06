package io.ashdavies.playground.conferences

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate

internal data class ConferenceYaml(
    @JsonProperty("name") val name: String,
    @JsonProperty("website") val website: String,
    @JsonProperty("location") val location: String,
    @JsonProperty("status") val status: String?,
    @JsonProperty("date_start") val dateStart: LocalDate,
    @JsonProperty("date_end") val dateEnd: LocalDate,
    @JsonProperty("online") val online: Boolean,
    @JsonProperty("cfp") val cfp: CfpYaml?,
) {

    data class CfpYaml(
        @JsonProperty("start") val start: LocalDate,
        @JsonProperty("end") val end: LocalDate,
        @JsonProperty("site") val site: String?,
    )
}
