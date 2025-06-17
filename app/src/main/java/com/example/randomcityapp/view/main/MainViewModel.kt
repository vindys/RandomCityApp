package com.example.randomcityapp.view.main

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.randomcityapp.domain.producer.RandomCityProducer
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.model.repository.CitiesRepository
import com.example.randomcityapp.model.source.local.RandomCity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CitiesRepository,
    private val producer: RandomCityProducer
) : ViewModel() {

    private val TAG = "MainViewModel"
    /*private val _state = MutableStateFlow(CitiesState())
    val state: StateFlow<CitiesState> = _state.asStateFlow()*/

    private val _firstEmission = MutableStateFlow<RandomCity?>(null)
    val firstEmission: StateFlow<RandomCity?> = _firstEmission.asStateFlow()

    /*private val _selectedCityId = MutableStateFlow<Int?>(null)
    val selectedCityId: StateFlow<Int?> = _selectedCityId.asStateFlow()*/

    /*fun selectCity(id: Int) {
        if(_selectedCityId.value != id)
            _selectedCityId.value = id
    }*/

    /*var hasStarted: Boolean = false

    val selectedItem: StateFlow<RandomCity?> = combine(
        state, selectedCityId
    ) { state, selectedId ->
        state.dataList.find { it.id == selectedId }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )*/

    //private val intentChannel = Channel<RandomCitiesIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            sendIntent(RandomCitiesIntent.ResetDb)
        }
        producer.startFirstEmission(viewModelScope)
        //processIntents()
    }

    fun startGenerator(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                producer.randomCityFlow()
                    .flowOn(Dispatchers.IO)
                    .collect { city ->
                        if (_firstEmission.value == null) {
                            _firstEmission.emit(city)  // first emission set here
                        }
                        withContext(Dispatchers.IO) {
                            Log.d(TAG, "startGenerator: insert : ${city.cityName}")
                            repository.insert(city)
                        }
                    }
            }
        }
    }

    fun sendIntent(intent: RandomCitiesIntent) {
        when (intent) {
            is RandomCitiesIntent.ResetDb -> resetDb()
            else -> {}
        }
    }

    fun resetDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetDb()
        }
    }

    /*private fun processIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is RandomCitiesIntent.LoadAll -> observeAllData()
                    is RandomCitiesIntent.SelectItem -> observeSelectedItem(intent.id)
                    is RandomCitiesIntent.ResetDb -> launch(Dispatchers.IO) {
                        repository.resetDb()
                    }
                }
            }
        }
    }

    private fun observeAllData() {
        viewModelScope.launch {
            repository.getAllCities().collect { list ->
                _state.update { it.copy(dataList = list) }
            }
        }
    }

    private fun observeSelectedItem(id: Int) {
        viewModelScope.launch {
            repository.getCityById(id).collect { item ->
                _state.update { it.copy(selectedItem = item) }
            }
        }
    }*/


    /*fun collectWhenInForeground(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                producer.randomCityFlow.collect { city ->
                    repository.insert(city)
                }
            }
        }
    }*/

}
