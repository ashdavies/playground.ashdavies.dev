package dev.ashdavies.nsd

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import java.net.Inet4Address
import java.net.Inet6Address
import android.net.nsd.NsdManager as AndroidNsdManager
import android.net.nsd.NsdServiceInfo as AndroidNsdServiceInfo

public actual typealias NsdManager = AndroidNsdManager

public actual typealias NsdServiceInfo = AndroidNsdServiceInfo

public actual fun NsdServiceInfo.getHostAddressOrNull(
    type: NsdHostAddress.Type,
): NsdHostAddress? = hostAddresses
    .firstOrNull {
        when (type) {
            NsdHostAddress.Type.IPv4 -> it is Inet4Address
            NsdHostAddress.Type.IPv6 -> it is Inet6Address
        }
    }
    ?.hostAddress
    ?.let { NsdHostAddress(it, type) }

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

public actual fun NsdManager.resolveService(serviceInfo: NsdServiceInfo): Flow<NsdServiceInfo> = callbackFlow {
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

    val dispatcher = coroutineContext[CoroutineDispatcher] ?: Dispatchers.Unconfined
    awaitClose { unregisterServiceInfoCallback(callback) }

    registerServiceInfoCallback(serviceInfo, dispatcher.asExecutor(), callback)
}
