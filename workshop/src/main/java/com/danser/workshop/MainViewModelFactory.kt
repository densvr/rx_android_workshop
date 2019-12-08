package com.danser.workshop

class MainViewModelFactory {

    fun toUi(words: List<Word>, filter: String): List<MainViewModel.WordItem> =
        words.map { it.toUi(filter) }

    private fun Word.toUi(selection: String = ""): MainViewModel.WordItem = MainViewModel.WordItem(
        text = this.word,
        selection = selection
    )
}
