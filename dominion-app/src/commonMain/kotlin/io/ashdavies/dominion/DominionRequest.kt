package io.ashdavies.dominion

import kotlinx.serialization.Serializable

@Serializable
internal sealed class DominionRequest(val format: String = "json") {

    @Serializable
    sealed class Query(val action: String = "query") : DominionRequest() {

        @Serializable
        data class Expansions(
            val titles: String = "Expansions",
            val pllimit: String = "max",
            val prop: String = "links",
        ) : Query()

        @Serializable
        data class Images(
            val titles: String,
            val prop: String = "imageinfo",
            val iiprop: String = "url"
        ) : Query()
    }

    @Serializable
    sealed class Parse(val action: String = "parse") : DominionRequest() {

        @Serializable
        data class Sections(
            val page: String,
            val prop: String = "sections"
        ) : Parse()

        @Serializable
        data class Section(
            val page: String,
            val section: String,
            val prop: String = "links"
        ) : Parse()
    }
}
