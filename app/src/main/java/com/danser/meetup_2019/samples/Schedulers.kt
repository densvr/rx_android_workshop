package com.danser.meetup_2019.samples

import java.lang.Thread.currentThread

fun loadItems() {
    uploadRepository.uploadImage(image: Image)
        .doOnNext { (image: Image, progress: Int) ->
            Log.d(TAG, "uploading on thread ${currentThread().name}")
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { item ->
            showBlurredImage(image)
            Log.d(TAG, "Showing image on thread ${currentThread().name}")
        }
        .observeOn(Schedulers.background())
        .flatMapSingle { (image, progress) ->
            if (progress == 100) {
                blurImage(image)
                    .doOnNext { Log.d(TAG, "show progress on thread ${currentThread().name}") }
                    .map { blurred -> blurred }
                    .onErrorReturn { image }
                    .subscribeOn(Schedulers.computation())
            } else {
                Single.just(image)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { image ->
                showBlurredImage(image)
                Log.d(TAG, "Showing image on thread ${currentThread().name}")
            },
            { th -> showError(th) }
        )
}
