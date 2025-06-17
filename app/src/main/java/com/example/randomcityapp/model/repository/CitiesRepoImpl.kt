package com.example.randomcityapp.model.repository

import com.example.randomcityapp.model.source.local.RandomCity
import com.example.randomcityapp.model.source.local.RandomCityDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepoImpl @Inject constructor(
    private val randomCityDao:
    RandomCityDao
) : CitiesRepository {
    override suspend fun insert(randomCity: RandomCity) {
        randomCityDao.insert(randomCity)
    }

    override suspend fun delete(randomCity: RandomCity) {
        randomCityDao.delete(randomCity)
    }

    override suspend fun update(randomCity: RandomCity) {
        randomCityDao.update(randomCity)
    }

    override fun getCityById(id: Int): Flow<RandomCity> = randomCityDao.getCitiesById(id)

    override fun getAllCities(): Flow<List<RandomCity>> = randomCityDao.getCities()

    override suspend fun resetDb() {
        randomCityDao.deleteAll()
        randomCityDao.clearPrimaryKeyIndex()
    }
}