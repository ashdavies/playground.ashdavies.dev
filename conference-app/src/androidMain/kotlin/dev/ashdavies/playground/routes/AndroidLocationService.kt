package dev.ashdavies.playground.routes

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import dev.ashdavies.content.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import kotlinx.coroutines.tasks.await

@ContributesBinding(AppScope::class, binding<LocationService>())
internal class AndroidLocationService @Inject constructor(context: PlatformContext) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    override suspend fun getLastLocation(): LatLng {
        val location = fusedLocationClient.lastLocation.await()
        return LatLng(location.latitude, location.longitude)
    }
}
