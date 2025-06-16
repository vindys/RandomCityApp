package com.example.randomcityapp.intent

import com.example.randomcityapp.model.source.local.RandomCity

data class CitiesState(
    val dataList: List<RandomCity> = emptyList(),
    val selectedItem: RandomCity? = null,
    val isLoading: Boolean = false
)
