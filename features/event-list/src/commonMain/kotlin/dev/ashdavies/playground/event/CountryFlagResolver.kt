package dev.ashdavies.playground.event

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

internal fun interface CountryFlagResolver {
    operator fun get(key: String): String?
}

@Inject
@ContributesBinding(AppScope::class)
internal class LocalCountryFlagResolver(
    private val store: Map<String, String> = mapOf(
        "Czech Republic" to "🇨🇿",
        "Germany" to "🇩🇪",
        "Kenya" to "🇰🇪",
        "India" to "🇮🇳",
        "Netherlands" to "🇳🇱",
        "Portugal" to "🇵🇹",
        "Uganda" to "🇺🇬",
        "UK" to "🇬🇧",
    ),
) : CountryFlagResolver {
    override operator fun get(key: String): String? = store[key]
}
