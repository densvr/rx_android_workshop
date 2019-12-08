package com.danser.workshop

import android.content.res.Resources

class MainComponent(private val resources: Resources) {

    val mainPresenter: MainPresenter by lazy {
        MainPresenter(
            mainViewModel = MainViewModel(
                state = MainViewModel.State.Loading,
                filter = "",
                items = emptyList()
            ),
            repo = WordsRepository(resources),
            vmFactory = MainViewModelFactory()
        )
    }
}
