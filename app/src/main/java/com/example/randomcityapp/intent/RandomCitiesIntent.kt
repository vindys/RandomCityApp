package com.example.randomcityapp.intent

sealed class RandomCitiesIntent {
    object LoadAll : RandomCitiesIntent() // todo do we need to make it generic type if multiple type responses comes
    data class SelectItem(val id: Int) : RandomCitiesIntent()
    object ResetDb : RandomCitiesIntent()
    //todo add a deselection intent
}
