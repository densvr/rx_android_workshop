package com.danser.meetup_2019.samples

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class SampleActivity: RxActivity() {

    @Inject
    lateinit var presenter: SamplePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.observeModel()
            .subscribeOn(Schedulers.io())
            .compose(bindToLifecycle())
            .subscribe(
                { userInfo -> bindUserInfo() },
                { th -> bindError() }
            )
    }
}

