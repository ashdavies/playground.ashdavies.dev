package io.ashdavies.party.tooling

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Day")
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
internal annotation class DayNightPreview
