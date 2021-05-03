package io.ashdavies.playground.yaml

@JsNonModule
@JsModule("yaml")
internal external object Yaml {
    fun parseAllDocuments(str: String): Array<Document.Parsed>
}

internal interface Document {
    interface Parsed : Document {
        fun toJSON(): dynamic
    }
}
