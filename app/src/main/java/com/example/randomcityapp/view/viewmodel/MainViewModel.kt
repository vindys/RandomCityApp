package com.example.randomcityapp.view.viewmodel

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CitiesRepository,
    private val producer: RandomCityProducer
) : ViewModel() {

    private val _firstEmission = MutableStateFlow<RandomCity?>(null)
    val firstEmission: StateFlow<RandomCity?> = _firstEmission.asStateFlow()

    private var generatorJob: Job? = null
    private val intentChannel = Channel<RandomCitiesIntent>(Channel.UNLIMITED)

    init {
        processIntents()
    }

    fun startGenerator(lifecycleOwner: LifecycleOwner) {
        generatorJob?.cancel()
        generatorJob = lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                producer.randomCityFlow()
                    .flowOn(Dispatchers.IO)
                    .collect { city ->
                        withContext(Dispatchers.IO) {
                            sendIntent(RandomCitiesIntent.insert(city))
                        }
                        if (_firstEmission.value == null) {
                            _firstEmission.emit(city)  // first emission set here
                        }

                    }
            }
        }
    }

    fun sendIntent(intent: RandomCitiesIntent) {
        intentChannel.trySend(intent).isSuccess // Optional: check result
    }

    private fun processIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is RandomCitiesIntent.insert -> launch(Dispatchers.IO) {
                        repository.insert(intent.randomCity)
                    }

                    is RandomCitiesIntent.ResetDb -> launch(Dispatchers.IO) {
                        repository.resetDb()
                    }

                    else -> {}
                }
            }
        }
    }
}
