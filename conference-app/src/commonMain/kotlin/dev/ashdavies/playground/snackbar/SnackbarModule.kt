package dev.ashdavies.playground.snackbar

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds

@ContributesTo(AppScope::class)
internal interface SnackbarModule {

    @Multibinds(allowEmpty = true)
    fun snackbarContributors(): Set<SnackbarContributor>
}
