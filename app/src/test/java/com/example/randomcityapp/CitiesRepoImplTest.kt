package com.example.randomcityapp
// CitiesRepoImplTest.kt


import com.example.randomcityapp.model.repository.CitiesRepoImpl
import com.example.randomcityapp.model.source.local.RandomCity
import com.example.randomcityapp.model.source.local.RandomCityDao
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesRepoImplTest {

    private lateinit var dao: RandomCityDao
    private lateinit var repository: CitiesRepoImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = CitiesRepoImpl(dao)
    }

    @Test
    fun `insert should call dao insert`() = runTest {
        val city = RandomCity(cityName = "City", color = "Red", time = 123L, lat = 0.0, lng = 0.0)
        repository.insert(city)
        coVerify { dao.insert(city) }
    }

    @Test
    fun `delete should call dao delete`() = runTest {
        val city = RandomCity(cityName = "City", color = "Red", time = 123L, lat = 0.0, lng = 0.0)
        repository.delete(city)
        coVerify { dao.delete(city) }
    }

    @Test
    fun `getCityById should return city flow from dao`() = runTest {
        val city =
            RandomCity(cityName = "City", color = "Red", time = 123L, lat = 0.0, lng = 0.0, id = 1)
        every { dao.getCitiesById(1) } returns flowOf(city)

        val result = repository.getCityById(1)
        result.collect {
            assert(it == city)
        }
    }

    @Test
    fun `resetDb should call deleteAll and clearPrimaryKeyIndex`() = runTest {
        repository.resetDb()
        coVerify { dao.deleteAll() }
        coVerify { dao.clearPrimaryKeyIndex() }
    }
}

