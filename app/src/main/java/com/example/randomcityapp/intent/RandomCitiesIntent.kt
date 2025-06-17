package com.example.randomcityapp.intent

import com.example.randomcityapp.model.source.local.RandomCity

sealed class RandomCitiesIntent {
    object LoadAll : RandomCitiesIntent()
    data class insert(val randomCity: RandomCity) : RandomCitiesIntent()
    data class SelectItem(val id: Int) : RandomCitiesIntent()
    object ResetDb : RandomCitiesIntent()
    //todo add a deselection intent
}
