package com.danser.meetup_2019.samples

import java.lang.Thread.currentThread

fun loadItems() {
    getItemsFromRemoteSource()
        .doOnNext { item -> Log.d(TAG, "Emitting item $item on thread ${currentThread().name}") }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { item -> Log.d(TAG, "Consuming item $item on thread ${currentThread().name}") }
        .subscribe { item -> showItem(item) }
}
