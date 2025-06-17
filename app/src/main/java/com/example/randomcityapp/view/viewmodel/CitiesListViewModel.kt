package com.example.randomcityapp.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomcityapp.intent.CitiesState
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.intent.RandomCitiesIntent.LoadAll
import com.example.randomcityapp.model.repository.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesListViewModel @Inject constructor(
    private val repository: CitiesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CitiesState())
    val state: StateFlow<CitiesState> = _state.asStateFlow()

    private val intentChannel = Channel<RandomCitiesIntent>(Channel.UNLIMITED)

    init {
        processIntents()
        sendIntent(LoadAll)
    }

    fun sendIntent(intent: RandomCitiesIntent) {
        intentChannel.trySend(intent).isSuccess // Optional: check result
    }

    private fun processIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is LoadAll -> observeAllData()
                    is RandomCitiesIntent.SelectItem -> selectCity(intent.id)
                    is RandomCitiesIntent.ResetDb -> launch(Dispatchers.IO) {
                        repository.resetDb()
                    }

                    is RandomCitiesIntent.insert -> launch(Dispatchers.IO) {
                        repository.insert(intent.randomCity)
                    }
                }
            }
        }
    }

    private fun observeAllData() {
        viewModelScope.launch {
            repository.getAllCities().collect { cities ->
                _state.update {
                    it.copy(dataList = cities)
                }
            }
        }
    }

    private fun selectCity(id: Int) {

        val currentState = state.value
        val cityInList = currentState.dataList.find { it.id == id }

        if (cityInList != null) {
            _state.update { it.copy(selectedItem = cityInList) }
        } else {
            viewModelScope.launch {
                repository.getCityById(id).firstOrNull()?.let { fallbackCity ->
                    _state.update { it.copy(selectedItem = fallbackCity) }
                }
            }
        }
    }

    fun clearSelection() {
        _state.update { it.copy(selectedItem = null) }
    }

}
