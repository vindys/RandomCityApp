package com.example.randomcityapp.domain.producer

import com.google.android.gms.maps.model.LatLng

interface GeocodingService {
    suspend fun getLatLng(cityName: String): LatLng?
}