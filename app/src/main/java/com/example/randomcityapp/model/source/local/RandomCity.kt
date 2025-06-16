package com.example.randomcityapp.model.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "random_cities",
)
data class RandomCity(
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "color", defaultValue = "")
    val color: String,
    @ColumnInfo(name = "time", defaultValue = "")
    val time: Long,
    val lat: Double,
    val lng: Double
)