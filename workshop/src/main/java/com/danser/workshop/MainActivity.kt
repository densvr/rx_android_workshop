package com.danser.workshop

import android.app.PendingIntent.getActivity
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
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

        etFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                presenter.onFilterTextChanged(p0?.toString() ?: "")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

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
                .observeOn(AndroidSchedulers.mainThread())
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

        if (etFilter.text.toString() != model.filter) {
            etFilter.setText(model.filter)
        }
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
        private val boldSpan = StyleSpan(Typeface.BOLD)
        fun update(item: MainViewModel.WordItem) = with(itemView) {
            if (item.selection != null) {
                tvText.text = SpannableStringBuilder(item.text).apply {
                    setSpan(boldSpan, 0, item.selection.length, SPAN_INCLUSIVE_INCLUSIVE)
                }
            } else {
                tvText.text = item.text
            }
        }
    }
}
