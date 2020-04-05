package com.example.revoluttestapp.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revoluttestapp.R
import com.example.revoluttestapp.app.RatesApplication
import com.example.revoluttestapp.core.di.app.ViewModelFactory
import com.example.revoluttestapp.domain.entity.CurrencyItem
import com.example.revoluttestapp.presentation.viewmodel.CurrentCurrencyState
import com.example.revoluttestapp.presentation.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import javax.inject.Inject

class RatesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: RatesViewModel
    private lateinit var ratesAdapter: RatesAdapter
    private val retryContainer : LinearLayout by lazy { findViewById<LinearLayout>(R.id.retry_container) }
    private val retryButton : Button by lazy { findViewById<Button>(R.id.retry_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RatesApplication.appComponent
            .ratesComponent()
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[RatesViewModel::class.java].apply {
            data.observe(this@RatesActivity, Observer(::showData))
            error.observe(this@RatesActivity, Observer(::showError))
        }

        savedInstanceState?.getParcelable<CurrentCurrencyState>(KEY_CURRENT_CURRENCY_STATE)?.let {
            viewModel.currentCurrencyState = it
        }

        initViews()
    }

    override fun onStart() {
        viewModel.subscribeToUpdates()
        super.onStart()
    }

    override fun onStop() {
        viewModel.unsubscribeToUpdates()
        super.onStop()
    }

    private fun initViews() {
        ratesAdapter = RatesAdapter(viewModel)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@RatesActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = ratesAdapter
        }
        retryButton.setOnClickListener({ viewModel.subscribeToUpdates() })
    }

    private fun showData(rates: List<CurrencyItem>?) {
        ratesAdapter.setItems(rates.orEmpty())
        retryContainer.visibility = View.GONE
    }

    private fun showError(msg: String) {
        retryContainer.visibility = View.VISIBLE
        retryContainer.errorText.setText(msg)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CURRENT_CURRENCY_STATE, viewModel.currentCurrencyState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_CURRENT_CURRENCY_STATE = "current_state"
    }
}
