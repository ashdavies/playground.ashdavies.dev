package io.ashdavies.playground.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import io.ashdavies.playground.R
import io.ashdavies.playground.compose.EmptyPainter
import io.ashdavies.playground.compose.fade
import io.ashdavies.playground.compose.isLoading
import io.ashdavies.playground.database.Profile
import io.ashdavies.playground.network.LocalHttpClient
import kotlin.random.Random.Default.nextInt

@Preview
@Composable
internal fun ProfileScreen() {
    val profileService = ProfileService(LocalHttpClient.current)
    val viewState: Profile by produceState(ProfileService.MaxMustermann) {
        value = profileService.getProfile()
    }

    val coilPainter: ImagePainter = rememberImagePainter(viewState.picture)

    Box(modifier = Modifier.padding(LocalScaffoldPadding.current)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier
                    .padding(top = 64.dp, bottom = 12.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 64.dp)
                            .padding(4.dp)
                            .fade(coilPainter.isLoading),
                        style = MaterialTheme.typography.h4,
                        text = viewState.name
                    )

                    viewState.position?.also {
                        Text(
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.padding(4.dp),
                            text = it,
                        )
                    }

                    viewState.location?.also {
                        Text(
                            style = MaterialTheme.typography.subtitle1,
                            text = it,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .padding(4.dp),
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = stringResource(R.string.events),
                style = MaterialTheme.typography.h4
            )

            FlowRow {
                repeat(nextInt(30)) {
                    Image(
                        painter = EmptyPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .border(1.dp, Color.LightGray, CircleShape)
                            .size(64.dp, 64.dp)
                            .clip(CircleShape)
                            .fade(true)
                            .padding(4.dp),
                    )
                }
            }
        }

        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Image(
                painter = coilPainter,
                contentDescription = null,
                modifier = Modifier
                    .fade(coilPainter.isLoading)
                    .align(CenterHorizontally)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .clip(CircleShape)
                    .size(128.dp)
            )
        }
    }
}
