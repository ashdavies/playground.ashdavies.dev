package io.ashdavies.tally.routes

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding

@ContributesBinding(AppScope::class, binding<LocationService>())
internal class NonAndroidLocationService @Inject constructor() : LocationService {
    override suspend fun getLastLocation(): LatLng = KnownLocations.Berlin
}
