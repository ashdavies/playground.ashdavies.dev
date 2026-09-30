package dev.ashdavies.playground.ui.snackbar

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

public interface SnackbarContributor {
    public val source: Flow<SnackbarEvent>
}

@Inject
@ContributesBinding(AppScope::class)
internal class CombinedSnackbarContributor(
    contributors: Set<SnackbarContributor>,
) : SnackbarContributor {
    override val source = contributors
        .map(SnackbarContributor::source)
        .merge()
}
