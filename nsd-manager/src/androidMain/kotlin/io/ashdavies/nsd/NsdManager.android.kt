package io.ashdavies.nsd

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.Inet6Address
import kotlin.coroutines.resumeWithException
import android.net.nsd.NsdManager as AndroidNsdManager
import android.net.nsd.NsdServiceInfo as AndroidNsdServiceInfo

public actual typealias NsdManager = AndroidNsdManager

public actual typealias NsdServiceInfo = AndroidNsdServiceInfo

public actual fun NsdServiceInfo.getHostAddressOrNull(
    type: NsdHostAddress.Type,
): NsdHostAddress? {
    val hostAddressList = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> hostAddresses
        else -> {
            @Suppress("DEPRECATION")
            listOf(host)
        }
    }

    return hostAddressList
        .firstOrNull {
            when (type) {
                NsdHostAddress.Type.IPv4 -> it is Inet4Address
                NsdHostAddress.Type.IPv6 -> it is Inet6Address
            }
        }
        ?.hostAddress
        ?.let { NsdHostAddress(it, type) }
}

public actual fun NsdManager.discoverServices(
    serviceType: String,
    protocolType: Int,
): Flow<List<NsdServiceInfo>> = channelFlow {
    val servicesFound = mutableListOf<NsdServiceInfo>()
    val listener = object : AndroidNsdManager.DiscoveryListener {
        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            cancel("Service start discovery failed ($errorCode)")
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            cancel("Service stop discovery failed ($errorCode)")
        }

        override fun onDiscoveryStarted(serviceType: String) = Unit

        override fun onDiscoveryStopped(serviceType: String) {
            channel.close()
        }

        override fun onServiceFound(serviceInfo: NsdServiceInfo) {
            servicesFound += serviceInfo
            trySend(servicesFound)
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo) {
            servicesFound -= serviceInfo
            trySend(servicesFound)
        }
    }

    discoverServices(
        /* serviceType = */ serviceType,
        /* protocolType = */ protocolType,
        /* listener = */ listener,
    )

    awaitClose {
        stopServiceDiscovery(listener)
    }
}

public actual fun NsdManager.resolveService(
    serviceInfo: NsdServiceInfo,
    coroutineDispatcher: CoroutineDispatcher,
): Flow<NsdServiceInfo> {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        return resolveServiceApi34(serviceInfo, coroutineDispatcher)
    }

    return flow {
        withContext(coroutineDispatcher) {
            emit(resolveServiceApi16(serviceInfo))
        }
    }
}

private suspend fun NsdManager.resolveServiceApi16(
    serviceInfo: NsdServiceInfo,
): NsdServiceInfo = suspendCancellableCoroutine { continuation ->
    val listener = object : AndroidNsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            continuation.resumeWithException(IllegalStateException("Service resolution failed with an error ($errorCode)"))
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            continuation.resumeWith(Result.success(serviceInfo))
        }
    }

    @Suppress("DEPRECATION")
    resolveService(
        /* serviceInfo = */ serviceInfo,
        /* listener = */ listener,
    )
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
private fun NsdManager.resolveServiceApi34(
    serviceInfo: NsdServiceInfo,
    coroutineDispatcher: CoroutineDispatcher,
): Flow<NsdServiceInfo> = callbackFlow {
    val callback = object : AndroidNsdManager.ServiceInfoCallback {
        override fun onServiceInfoCallbackRegistrationFailed(errorCode: Int) {
            cancel("Service info callback registration failed ($errorCode)")
        }

        override fun onServiceUpdated(serviceInfo: NsdServiceInfo) {
            trySend(serviceInfo)
        }

        override fun onServiceLost() = Unit

        override fun onServiceInfoCallbackUnregistered() {
            channel.close()
        }
    }

    registerServiceInfoCallback(
        /* serviceInfo = */ serviceInfo,
        /* executor = */ coroutineDispatcher.asExecutor(),
        /* listener = */ callback,
    )

    awaitClose { unregisterServiceInfoCallback(callback) }
}
