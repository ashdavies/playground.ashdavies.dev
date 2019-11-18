package io.ashdavies.playground.common

internal class NotEmptyString(
    private val value: String
) : CharSequence by value,
    Comparable<String> by value {

  init {
    if (value.isEmpty()) {
      throw IllegalArgumentException("Value cannot be empty")
    }
  }
}