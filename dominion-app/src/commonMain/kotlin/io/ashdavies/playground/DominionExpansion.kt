package io.ashdavies.playground

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
internal data class DominionExpansion(val name: String, val image: String) : Parcelable
