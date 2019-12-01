package com.danser.workshop

class MainPresenter(
    private val view: MainView,
    private var mainViewModel: MainViewModel =

) {

    init {
        view.update(MainViewModel(
            state =
        ))
    }

    private fun getItems() {

    }



}
