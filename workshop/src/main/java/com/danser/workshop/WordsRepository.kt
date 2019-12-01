package com.danser.workshop

import android.content.res.Resources
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


interface IWordsRepository {
    fun loadWords(): Completable
    fun observeWords(): Observable<List<Word>>
}

class WordsRepository(resources: Resources): IWordsRepository {

    private val wordsSubject = BehaviorSubject.create<List<Word>>()

    override fun loadWords(): Completable {

    }

    override fun observeWords(): Observable<List<Word>> {

    }


}
