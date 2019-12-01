package com.danser.meetup_2019.samples

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class SampleActivity: AppCompatActivity() {

    @Inject
    lateinit var presenter: SamplePresenter

    private var modelSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ComponentManager.mainComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        modelSubscription?.unsubscribe()
    }

    private fun bindUserInfo() { ... }

    private fun bindError() { ... }
}

interface SampleView {

}
