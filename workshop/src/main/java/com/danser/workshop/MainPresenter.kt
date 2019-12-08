package com.danser.workshop

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class MainPresenter(
    private var mainViewModel: MainViewModel,
    private val repo: IWordsRepository,
    private val vmFactory: MainViewModelFactory
) {

    private var loadWordsDisposable: Disposable? = null

    private val viewModelSubject: BehaviorSubject<MainViewModel> = BehaviorSubject.create()

    init {
        viewModelSubject.onNext(mainViewModel)
        loadWords()
    }

    fun observeViewModels(): Observable<MainViewModel> = viewModelSubject


    private fun loadWords() {
        updateModel { copy(state = MainViewModel.State.Loading) }
        loadWordsDisposable?.dispose()
        loadWordsDisposable = repo.loadWords().subscribe(
            { items ->
                if (items.isEmpty()) {
                    updateModel {
                        copy(
                            state = MainViewModel.State.Empty,
                            items = emptyList()
                        )
                    }
                } else {
                    updateModel {
                        copy(
                            state = MainViewModel.State.Content,
                            items = vmFactory.toUi(items)
                        )
                    }
                }
            },
            { th ->
                updateModel { copy(state = MainViewModel.State.Error("Check your internet connection")) }
            }
        )
    }

    private fun updateModel(mapper: MainViewModel.() -> MainViewModel = { this }) {
        mainViewModel = mainViewModel.mapper()
        viewModelSubject.onNext(mainViewModel)
    }
}
