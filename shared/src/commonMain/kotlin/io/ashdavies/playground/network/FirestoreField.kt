package io.ashdavies.playground.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class FirestoreField {

    @Serializable
    data class BooleanField(

        @SerialName("booleanValue")
        val value: Boolean
    ) : FirestoreField()

    @Serializable
    data class DateField(

        @SerialName("stringValue")
        @Serializable(LocalDateSerializer::class)
        val value: LocalDate
    ) : FirestoreField()

    @Serializable
    data class DateTimeField(

        @SerialName("stringValue")
        @Serializable(LocalDateTimeSerializer::class)
        val value: LocalDateTime
    )

    @Serializable
    data class IntegerField(

        @SerialName("integerValue")
        val value: Int
    ) : FirestoreField()

    @Serializable
    data class StringField(

        @SerialName("stringValue")
        val value: String
    ) : FirestoreField()
}
