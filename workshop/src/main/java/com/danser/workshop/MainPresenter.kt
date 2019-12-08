package com.danser.workshop

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainPresenter(
    private var mainViewModel: MainViewModel,
    private val repo: IWordsRepository,
    private val vmFactory: MainViewModelFactory
) {
    private var filterSubject: PublishSubject<String> = PublishSubject.create()
    private val viewModelSubject: BehaviorSubject<MainViewModel> = BehaviorSubject.create()

    private var loadWordsDisposable: Disposable? = null
    private var filterDisposable: Disposable? = null

    init {
        viewModelSubject.onNext(mainViewModel)
        loadWords()
        subscribeToFilter()
    }

    fun observeViewModels(): Observable<MainViewModel> = viewModelSubject

    fun onFilterTextChanged(text: String) {
        filterSubject.onNext(text)
    }

    fun onDestroy() {
        loadWordsDisposable?.dispose()
        filterDisposable?.dispose()
    }

    private fun loadWords() {
        updateModel { copy(state = MainViewModel.State.Loading) }
        loadWordsDisposable?.dispose()
        loadWordsDisposable = repo.loadWords()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { items ->
                    updateModel(items)
                },
                { th ->
                    updateModel { copy(state = MainViewModel.State.Error("Check your internet connection")) }
                }
            )
    }

    private fun subscribeToFilter() {
        filterDisposable?.dispose()
        filterDisposable = filterSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .flatMapSingle { filter -> repo.loadWords()
                .map { words -> words.filter { it.word.startsWith(filter) }}
                .map { words -> words to filter } }
            .onErrorReturn { emptyList<Word>() to "" }
            .subscribeOn(Schedulers.io())
            .subscribe { (items, filter) ->
                updateModel(items, filter)
            }
    }


    private fun updateModel(items: List<Word>, filter: String = "") {
        if (items.isEmpty()) {
            updateModel {
                copy(
                    state = MainViewModel.State.Empty,
                    filter = filter,
                    items = emptyList()
                )
            }
        } else {
            updateModel {
                copy(
                    state = MainViewModel.State.Content,
                    filter = filter,
                    items = vmFactory.toUi(items, filter)
                )
            }
        }
    }

    private fun updateModel(mapper: MainViewModel.() -> MainViewModel = { this }) {
        mainViewModel = mainViewModel.mapper()
        viewModelSubject.onNext(mainViewModel)
    }
}
