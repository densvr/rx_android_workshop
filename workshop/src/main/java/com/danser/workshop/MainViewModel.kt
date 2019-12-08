package com.danser.workshop

data class MainViewModel(
    val state: State,
    val filter: String,
    val items: List<WordItem>
) {

    sealed class State {
        object Loading : State()
        class Error(val text: String) : State()
        object Content : State()
        object Empty : State()
    }

    data class WordItem(val text: String, val selection: String)
}
