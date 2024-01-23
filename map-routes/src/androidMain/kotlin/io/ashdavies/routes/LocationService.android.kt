package io.ashdavies.routes

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.tasks.await

internal actual fun LocationService(context: PlatformContext): LocationService {
    return AndroidLocationService(context)
}

private class AndroidLocationService(context: PlatformContext) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    override suspend fun getLastLocation(): LatLng {
        val location = fusedLocationClient.lastLocation.await()
        return LatLng(location.latitude, location.longitude)
    }
}
