package io.ashdavies.playground.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent

internal class MainActivity : AppCompatActivity() {

    //private val navController: NavController by navController(R.id.host)
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }
}
