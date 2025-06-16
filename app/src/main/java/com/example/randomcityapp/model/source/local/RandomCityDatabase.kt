package com.example.randomcityapp.model.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RandomCity::class], version = 1)
abstract class RandomCityDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, RandomCityDatabase::class.java, "cities")
                .build()
    }

    abstract fun getRandoCityDao(): RandomCityDao

}