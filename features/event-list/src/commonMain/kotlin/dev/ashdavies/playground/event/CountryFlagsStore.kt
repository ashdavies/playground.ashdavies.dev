package dev.ashdavies.playground.event

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Qualifier

@Qualifier
internal annotation class CountryFlagStore

@BindingContainer
@ContributesTo(AppScope::class)
internal object CountryFlagsBindings {

    @Provides
    @CountryFlagStore
    fun provideCountryFlagStore(): Map<String, String> = mapOf(
        "Czech Republic" to "🇨🇿",
        "Germany" to "🇩🇪",
        "Kenya" to "🇰🇪",
        "India" to "🇮🇳",
        "Netherlands" to "🇳🇱",
        "Portugal" to "🇵🇹",
        "Uganda" to "🇺🇬",
        "UK" to "🇬🇧",
    )
}
