package com.codingeveryday.calcapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.domain.entities.HistoryItem

class HistoryItemAdapter: ListAdapter<HistoryItem, HistoryItemViewHolder>(HistoryItemDiffCallback()) {

    var onExprClickListener: ((HistoryItem) -> Unit)? = null
    var onResClickListener: ((HistoryItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.exprField.text = getItem(position).expr
        holder.resField.text = getItem(position).result
        holder.exprField.setOnClickListener {
            onExprClickListener?.invoke(getItem(position))
        }
        holder.resField.setOnClickListener {
            onResClickListener?.invoke(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    companion object {
        const val POOL_SIZE = 20
        const val VIEW_TYPE = 1
    }
}