package com.example.randomcityapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.randomcityapp.model.repository.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityDetailsViewModel @Inject constructor(
    private val repository: CitiesRepository
) : ViewModel()


