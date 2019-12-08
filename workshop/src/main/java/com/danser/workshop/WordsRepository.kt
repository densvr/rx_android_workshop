package com.danser.workshop

import android.content.res.Resources
import io.reactivex.Single


interface IWordsRepository {
    fun loadWords(): Single<List<Word>>
}

class WordsRepository(private val resources: Resources) : IWordsRepository {
    override fun loadWords(): Single<List<Word>> = Single
        .fromCallable { resources.getStringArray(R.array.common_words).map { Word(it) } }
        .cache()
}
