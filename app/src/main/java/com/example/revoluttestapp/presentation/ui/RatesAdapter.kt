package com.example.revoluttestapp.presentation.ui

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.revoluttestapp.R
import com.example.revoluttestapp.domain.entity.BaseCurrencyItem
import com.example.revoluttestapp.domain.entity.CurrencyItem
import com.example.revoluttestapp.presentation.utils.CurrencyDiffUtilCallback
import com.example.revoluttestapp.presentation.utils.CurrencyTextWatcher
import com.example.revoluttestapp.presentation.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.item_currency.view.*
import java.math.BigDecimal

class RatesAdapter(
    private val viewModel: RatesViewModel
) : RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    companion object {
        const val PAYLOAD_NEW_AMOUNT = 0
        const val PAYLOAD_NEW_CURRENCY = 1
    }

    var items = listOf<CurrencyItem>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            when (payloads[0]) {
                PAYLOAD_NEW_AMOUNT -> {
                    holder.bindAmount(items[position].amount)
                }
                PAYLOAD_NEW_CURRENCY -> {
                    holder.bindBaseCurrency(viewModel, items[position])
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(currencyItems: List<CurrencyItem>) {
        val diff = DiffUtil.calculateDiff(
            CurrencyDiffUtilCallback(
                items,
                currencyItems
            )
        )
        items = currencyItems
        diff.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var textWatcher: TextWatcher? = null

        fun bind(viewModel: RatesViewModel, currencyItem: CurrencyItem) {
            removeAmountTextWatcher()
            bindCurrency(currencyItem)
            bindAmount(currencyItem.amount)
            when (currencyItem) {
                is BaseCurrencyItem -> {
                    bindBaseCurrency(viewModel, currencyItem)
                } else -> {
                    bindExchangeCurrency(viewModel, currencyItem)
                }
            }
        }

        private fun bindCurrency(currencyItem: CurrencyItem) {
            with(currencyItem.currency) {
                itemView.currencyCode.text = code
                itemView.currencyName.text = name
                if (image != -1) {
                    itemView.currencyImage.setImageResource(image)
                } else {
                    itemView.currencyImage.setImageResource(android.R.color.transparent)
                }
            }
        }

        fun bindBaseCurrency(viewModel: RatesViewModel, currencyItem: CurrencyItem) {
            itemView.setOnClickListener(null)
            itemView.amount.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setupAmountEditText(viewModel, currencyItem)
                } else {
                    removeAmountTextWatcher()
                    (itemView.parent as ViewGroup).isFocusable = false
                }
            }
        }

        private fun bindExchangeCurrency(viewModel: RatesViewModel, currencyItem: CurrencyItem) {
            itemView.setOnClickListener {
                (itemView.parent as View).clearFocus()
                itemView.amount.requestFocus()
                onNewAmount(viewModel, currencyItem)
            }
            itemView.amount.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setupAmountEditText(viewModel, currencyItem)
                    onNewAmount(viewModel, currencyItem)
                } else {
                    removeAmountTextWatcher()
                }
            }
        }

        fun bindAmount(newAmount: BigDecimal) {
            val currencyAmountValue = newAmount.toString()
            if (currencyAmountValue == "0") {
                setLightAmountColor()
            } else {
                setMainAmountColor()
            }
            itemView.amount.setText(currencyAmountValue)
        }

        private fun onNewAmount(viewModel: RatesViewModel, currencyItem: CurrencyItem) {
            val inputtedAmount = itemView.amount.text.toString()
            viewModel.onNewAmount(currencyItem, inputtedAmount)
        }

        private fun setupAmountEditText(viewModel: RatesViewModel, currencyItem: CurrencyItem) {
            setMainAmountColor()
            textWatcher = CurrencyTextWatcher(itemView.amount) {
                onNewAmount(viewModel, currencyItem)
            }
            itemView.amount.apply {
                addTextChangedListener(textWatcher)
            }
        }

        private fun removeAmountTextWatcher() {
            textWatcher?.let {
                itemView.amount.removeTextChangedListener(it)
            }
        }

        private fun setMainAmountColor() {
            val textColor = ContextCompat.getColor(itemView.context, R.color.colorBlack)
            itemView.amount.setTextColor(textColor)
        }

        private fun setLightAmountColor() {
            val textColor = ContextCompat.getColor(itemView.context, R.color.colorGreyLight)
            itemView.amount.setTextColor(textColor)
        }
    }
}