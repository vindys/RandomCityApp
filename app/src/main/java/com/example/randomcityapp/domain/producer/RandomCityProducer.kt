package com.example.randomcityapp.domain.producer

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.example.randomcityapp.model.source.local.RandomCity
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

class RandomCityProducer  @Inject constructor(
    private val geocodingService: GeocodingService,
    @Named("cityNames") private val cityNames: List<String>,
    @Named("colors") private val colors: List<String>
) {
    private val _randomCityFlow = MutableSharedFlow<RandomCity>(replay = 0)
    val randomCityFlow: SharedFlow<RandomCity> = _randomCityFlow

    private val _firstEmission = MutableStateFlow<RandomCity?>(null)
    val firstEmission: StateFlow<RandomCity?> = _firstEmission

    fun startFirstEmission(scope: CoroutineScope) {
        if (_firstEmission.value != null) return
        scope.launch(Dispatchers.IO) {
            if (_firstEmission.value == null) {
                val city = generateRandomCity()
                _firstEmission.emit(city)
            }
        }
    }

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
        val latLng = geocodingService.getLatLng(cityName)?: LatLng(0.0, 0.0)

        return RandomCity(cityName = cityName, color = color,
            time = time, lat = latLng.latitude, lng = latLng.longitude)
    }


}
