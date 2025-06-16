package com.example.randomcityapp.model.repository

import com.example.randomcityapp.model.source.local.RandomCity
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    suspend fun insert(city: RandomCity)

    suspend fun delete(city: RandomCity)

    suspend fun update(city: RandomCity)

    fun getCityById(id: Int): Flow<RandomCity>

    fun getAllCities(): Flow<List<RandomCity>>

    suspend fun resetDb()
}