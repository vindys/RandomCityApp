package com.example.randomcityapp.di

import android.content.Context
import com.example.randomcityapp.domain.producer.AndroidGeocodingService
import com.example.randomcityapp.domain.producer.GeocodingService
import com.example.randomcityapp.model.repository.CitiesRepoImpl
import com.example.randomcityapp.model.repository.CitiesRepository
import com.example.randomcityapp.model.source.local.RandomCityDao
import com.example.randomcityapp.model.source.local.RandomCityDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ModelModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RandomCityDatabase {
        return RandomCityDatabase.getInstance(context)
    }

    @Provides
    fun provideDao(randomCityDatabase: RandomCityDatabase): RandomCityDao {
        return randomCityDatabase.getRandoCityDao()
    }


    @Provides
    fun provideRepository(randomCityDao: RandomCityDao): CitiesRepository {
        return CitiesRepoImpl(randomCityDao)
    }

    @Provides
    fun provideGeocodingService(
        @ApplicationContext context: Context
    ): GeocodingService {
        return AndroidGeocodingService(context)
    }

    @Provides
    @Named("cityNames")
    fun provideCityNames(): List<String> = listOf("New York", "Los Angeles", "Scranton", "Philadelphia",
        "Nashville", "Saint Louis", "Miami")

    @Provides
    @Named("colors")
    fun provideColors(): List<String> = listOf("Yellow", "White", "Green", "Blue", "Red", "Black")
}