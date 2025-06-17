package com.example.randomcityapp.domain.producer

import com.example.randomcityapp.model.source.local.RandomCity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class RandomCityProducer @Inject constructor(
    private val geocodingService: GeocodingService,
    @Named("cityNames") private val cityNames: List<String>,
    @Named("colors") private val colors: List<String>
) {
    fun randomCityFlow(): Flow<RandomCity> = flow {
        while (true) {
            delay(5000)
            val city = generateRandomCity()
            emit(city)
        }
    }

    suspend fun generateRandomCity(): RandomCity {
        val cityName = cityNames.random()
        val color = colors.random()
        val time = System.currentTimeMillis()
        val latLng = geocodingService.getLatLng(cityName) ?: LatLng(0.0, 0.0)

        return RandomCity(
            cityName = cityName, color = color,
            time = time, lat = latLng.latitude, lng = latLng.longitude
        )
    }


}
