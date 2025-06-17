package com.example.randomcityapp

import com.example.randomcityapp.domain.producer.GeocodingService
import com.example.randomcityapp.domain.producer.RandomCityProducer
import com.example.randomcityapp.model.source.local.RandomCity
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class RandomCityProducerTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var producer: RandomCityProducer
    private val fakeGeocodingService = mockk<GeocodingService>()

    private val cityNames = listOf("New York", "Los Angeles", "Scranton", "Philadelphia",
        "Nashville", "Saint Louis", "Miami","TestCity")
    private val colors = listOf("Yellow", "White", "Green", "Blue", "Red", "Black")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        producer = RandomCityProducer(fakeGeocodingService, listOf("TestCity"), listOf("Red"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `generateRandomCity returns city with real latLng if geocoding succeeds`() = runTest {
        // Given
        val expectedLatLng = LatLng(37.7749, -122.4194)
        coEvery { fakeGeocodingService.getLatLng(any()) } returns expectedLatLng
        //when
        val city = producer.generateRandomCity()

        // Then
        assertEquals(expectedLatLng.latitude, city.lat, 0.001)
        assertEquals(expectedLatLng.longitude, city.lng, 0.001)
        assertEquals("TestCity", city.cityName)
    }

    @Test
    fun `generateRandomCity returns city with valid latLng`() = runTest {
        val latLng = LatLng(12.34, 56.78)
        coEvery { fakeGeocodingService.getLatLng(any()) } returns latLng

        val city = producer.generateRandomCity()

        assertTrue(cityNames.contains(city.cityName))
        assertTrue(colors.contains(city.color))
        assertEquals(12.34, city.lat, 0.001)
        assertEquals(56.78, city.lng, 0.001)
    }


    @Test
    fun `generateRandomCity uses default latLng when geocoding fails`() = runTest {
        // Given
        coEvery { fakeGeocodingService.getLatLng(any()) } returns null

        // When
        val city = producer.generateRandomCity()

        // Then
        assertEquals(0.0, city.lat, 0.001)
        assertEquals(0.0, city.lng, 0.001)
    }

    @Test
    fun `randomCityFlow emits cities periodically`() = runTest {
        val latLng = LatLng(1.0, 2.0)
        coEvery { fakeGeocodingService.getLatLng(any()) } returns latLng

        val cities = mutableListOf<RandomCity>()
        val job = launch {
            producer.randomCityFlow().take(2).collect { city ->
                cities.add(city)
            }
        }

        advanceTimeBy(5000 * 2)
        job.join()

        assertEquals(2, cities.size)
    }

    @Test
    fun `generateRandomCity should return valid RandomCity`() = runTest {
        val expectedLatLng = LatLng(37.7749, -122.4194)
        coEvery { fakeGeocodingService.getLatLng(any()) } returns expectedLatLng

        val city = producer.generateRandomCity()

        assertEquals("TestCity", city.cityName)
        assertEquals("Red", city.color)
        assertEquals(37.7749, city.lat, 0.01)
        assertEquals(-122.4194, city.lng, 0.01)
    }


}
