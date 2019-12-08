package com.danser.workshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_word.view.*

class MainActivity : AppCompatActivity() {

    private val adapter = Adapter()
    private val presenter = MainApplication.MAIN_COMPONENT.mainPresenter
    private var viewModelDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this@MainActivity)

        subscribeToViewModels()
    }

    override fun onStart() {
        super.onStart()
        subscribeToViewModels()
    }

    override fun onStop() {
        super.onStop()
        unsubscribeFromViewModels()
    }

    private fun subscribeToViewModels() {
        if (viewModelDisposable == null) {
            viewModelDisposable = presenter
                .observeViewModels()
                .distinctUntilChanged()
                .subscribe { update(it) }
        }
    }

    private fun unsubscribeFromViewModels() {
        viewModelDisposable?.dispose()
    }

    private fun update(model: MainViewModel) {
        vLoading.visibility(model.state is MainViewModel.State.Loading)
        vError.visibility(model.state is MainViewModel.State.Error)
        rvList.visibility(model.state is MainViewModel.State.Content)
        vEmpty.visibility(model.state is MainViewModel.State.Empty)

        vError.text = if (model.state is MainViewModel.State.Error) model.state.text else ""

        etFilter.setText(model.filter)
        adapter.swapData(model.items)
    }

    private fun View.visibility(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    class Adapter : RecyclerView.Adapter<WordItemViewHolder>() {

        private var items: List<MainViewModel.WordItem> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_word, parent, false)
            return WordItemViewHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) {
            holder.update(items[position])
        }

        fun swapData(items: List<MainViewModel.WordItem>) {
            this.items = items
            notifyDataSetChanged()
        }
    }

    class WordItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun update(item: MainViewModel.WordItem) = with(itemView) {
            tvText.text = item.text
        }
    }
}
