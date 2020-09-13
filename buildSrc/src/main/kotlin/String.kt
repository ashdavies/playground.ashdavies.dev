import java.util.Locale.UK
import java.util.regex.Pattern

private val CAMEL_CASE_PATTERN = Pattern.compile("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")

internal inline class CamelCaseString(private val value: String) {

    fun toKebabCase(): String = value
        .split(CAMEL_CASE_PATTERN)
        .joinToString("-") { it.toLowerCase(UK) }
}

internal val String.group get() = substringBefore('-')

internal val String.version get() = substringAfter('-')
