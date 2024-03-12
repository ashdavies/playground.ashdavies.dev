package io.ashdavies.nsd

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

private const val HTTP_TCP = "_http._tcp"
private const val PROTOCOL_DNS_SD = 1

public fun interface NsdAgent {
    public fun resolve(serviceName: String): Flow<NsdState>
}

public sealed interface NsdState {

    public data object Discovering : NsdState

    public data class Discovered(
        val services: List<String>,
    ) : NsdState

    public data class Resolved(
        val serviceName: String,
        val hostAddress: String,
        val port: Int,
    ) : NsdState
}

public fun NsdAgent(manager: NsdManager): NsdAgent = NsdAgent { serviceName ->
    channelFlow {
        send(NsdState.Discovering)

        @OptIn(ExperimentalCoroutinesApi::class)
        manager
            .discoverServices(HTTP_TCP, PROTOCOL_DNS_SD)
            .flatMapLatest { serviceList ->
                val serviceInfo = serviceList.filter { it.getServiceName() == serviceName }
                val serviceNames = serviceInfo.map { it.getServiceName() }

                send(NsdState.Discovered(serviceNames))
                serviceInfo.asFlow()
            }
            .flatMapMerge { serviceInfo ->
                manager.resolveService(serviceInfo)
            }
            .collect { serviceInfo ->
                val hostAddress = serviceInfo.getHostAddressOrNull()
                if (hostAddress != null) {
                    val resolved = NsdState.Resolved(
                        serviceName = serviceInfo.getServiceName(),
                        hostAddress = hostAddress.value,
                        port = serviceInfo.getPort(),
                    )

                    send(resolved)
                }
            }
    }
}
