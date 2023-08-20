package io.ashdavies.nsd

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

public data class NsdHostAddress(
    public val value: String,
    public val type: Type,
) {
    public enum class Type {
        IPv4,
        IPv6,
    }
}

public expect class NsdManager

public expect class NsdServiceInfo {
    public fun getServiceName(): String
    public fun getPort(): Int
}

public expect fun NsdServiceInfo.getHostAddressOrNull(
    type: NsdHostAddress.Type = NsdHostAddress.Type.IPv4,
): NsdHostAddress?

public expect fun NsdManager.discoverServices(
    serviceType: String,
    protocolType: Int,
): Flow<List<NsdServiceInfo>>

public expect fun NsdManager.resolveService(
    serviceInfo: NsdServiceInfo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): Flow<NsdServiceInfo>
