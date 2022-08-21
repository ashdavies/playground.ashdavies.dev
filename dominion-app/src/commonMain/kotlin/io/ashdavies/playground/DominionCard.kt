package io.ashdavies.playground

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize internal data class DominionCard(val expansion: DominionExpansion, val name: String, val image: String? = null, var ILLEGAL_MUTABLE: MutableSet<String> = mutableSetOf()) : Parcelable
