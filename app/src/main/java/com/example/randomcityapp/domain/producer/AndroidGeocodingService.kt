package com.example.randomcityapp.domain.producer

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class AndroidGeocodingService @Inject constructor(
    @ApplicationContext private val context: Context
) : GeocodingService {

    override suspend fun getLatLng(cityName: String): LatLng? {
        return getLatLngFromCityName(context, cityName) // your utility
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getLatLngFromCityName(context: Context, cityName: String): LatLng? {
        val geocoder = Geocoder(context, Locale.getDefault())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocationName(cityName, 1) { results ->
                    if (cont.isActive) {
                        val location = results.firstOrNull()
                        if (location != null) {
                            cont.resume(LatLng(location.latitude, location.longitude),
                                onCancellation = null)
                        } else {
                            cont.resume(null,
                                onCancellation = null)
                        }
                    }
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                try {
                    val results = geocoder.getFromLocationName(cityName, 1)
                    results?.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
                } catch (e: IOException) {
                    null
                }
            }
        }
    }
}