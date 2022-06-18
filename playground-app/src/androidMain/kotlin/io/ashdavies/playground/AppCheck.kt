package io.ashdavies.playground

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProvider
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.AppCheckToken
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.internal.DefaultAppCheckToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.asTask

@Composable
public actual fun AppCheckListener(listener: (String) -> Unit) {
    val appCheckService: AppCheckService = rememberAppCheckService()
    val appCheck: FirebaseAppCheck = rememberFirebaseAppCheck()

    LaunchedEffect(Unit) {
        appCheck.installAppCheckProviderFactory(TokenAppCheckProvider.Factory(appCheckService))
        appCheck.addAppCheckTokenListener { it.token }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private class TokenAppCheckProvider(private val appCheckService: AppCheckService) : AppCheckProvider {
    override fun getToken(): Task<AppCheckToken> = GlobalScope.async {
        with(appCheckService.createToken()) { DefaultAppCheckToken.constructFromRawToken(token) }
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

