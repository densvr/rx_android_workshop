package com.danser.meetup_2019.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SampleActivity: AppCompatActivity() {

    private val presenter: SamplePresenter = SamplePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.observeModel()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.main())
            .subscribe(
                { userInfo -> bindUserInfo() },
                { th -> bindError() }
            )
    }

    private fun bindUserInfo() { ... }

    private fun bindError() { ... }
}
