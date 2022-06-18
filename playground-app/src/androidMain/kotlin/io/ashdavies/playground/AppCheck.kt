package io.ashdavies.playground

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProvider
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.AppCheckToken
import com.google.firebase.appcheck.FirebaseAppCheck
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.asTask

@Composable
public fun ProvideAppCheckToken(content: @Composable () -> Unit) {
    var _appCheckToken by remember { mutableStateOf<String?>(null) }
    val appCheckService: AppCheckService = rememberAppCheckService()
    val appCheck: FirebaseAppCheck = rememberFirebaseAppCheck()
    val appCheckToken = _appCheckToken

    LaunchedEffect(Unit) {
        appCheck.installAppCheckProviderFactory(TokenAppCheckProvider.Factory(appCheckService))
        appCheck.addAppCheckTokenListener { _appCheckToken = it.token }
    }

    if (appCheckToken != null) {
        CompositionLocalProvider(LocalAppCheckToken provides appCheckToken) {
            content()
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private class TokenAppCheckProvider(private val appCheckService: AppCheckService) : AppCheckProvider {
    override fun getToken(): Task<AppCheckToken> = GlobalScope.async {
        appCheckService.createToken()
    }.asTask()

    class Factory(private val appCheckService: AppCheckService) : AppCheckProviderFactory {
        override fun create(firebaseApp: FirebaseApp): AppCheckProvider = TokenAppCheckProvider(appCheckService)
    }
}

@Composable
private fun rememberFirebaseAppCheck(firebaseApp: FirebaseApp = rememberFirebaseApp()): FirebaseAppCheck =
    remember(firebaseApp) { FirebaseAppCheck.getInstance(firebaseApp) }

@Composable
private fun rememberFirebaseApp(context: Context = LocalContext.current): FirebaseApp =
    remember(context) { FirebaseApp.initializeApp(context) ?: FirebaseApp.getInstance() }

