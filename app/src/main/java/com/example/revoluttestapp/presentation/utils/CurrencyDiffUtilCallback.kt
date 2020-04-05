package com.example.revoluttestapp.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.revoluttestapp.domain.entity.BaseCurrencyItem
import com.example.revoluttestapp.domain.entity.CurrencyItem
import com.example.revoluttestapp.presentation.ui.RatesAdapter

class CurrencyDiffUtilCallback(
    private val oldList: List<CurrencyItem>,
    private val newList: List<CurrencyItem>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.currency.code == newItem.currency.code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is BaseCurrencyItem && newItem is BaseCurrencyItem) {
           true
        } else if (oldItem !is BaseCurrencyItem && newItem !is BaseCurrencyItem) {
            oldItem.currency.code == newItem.currency.code &&
                    oldItem.amount.toString() == newItem.amount.toString()
        } else {
            false
        }
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem !is BaseCurrencyItem && newItem !is BaseCurrencyItem) {
            RatesAdapter.PAYLOAD_NEW_AMOUNT
        } else if (oldItem !is BaseCurrencyItem && newItem is BaseCurrencyItem) {
            RatesAdapter.PAYLOAD_NEW_CURRENCY
        } else {
            null
        }
    }
}