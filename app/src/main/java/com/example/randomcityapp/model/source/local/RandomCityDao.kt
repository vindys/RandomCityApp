package com.example.randomcityapp.model.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(randomCity: RandomCity)

    @Update
    suspend fun update(randomCity: RandomCity)

    @Delete
    suspend fun delete(randomCity: RandomCity)

    @Query("SELECT * FROM random_cities where id = :id")
    fun getCitiesById(id:Int): Flow<RandomCity>

    @Query("SELECT * FROM random_cities ORDER BY city_name ASC")
    fun getCities(): Flow<List<RandomCity>>

    @Query("DELETE FROM random_cities")
    fun deleteAll()

    @Query("DELETE FROM sqlite_sequence")
    fun clearPrimaryKeyIndex()
}