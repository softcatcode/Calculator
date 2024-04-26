package com.codingeveryday.calcapp.presentation.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.presentation.main.adapters.diffUtils.HistoryItemDiffCallback
import com.codingeveryday.calcapp.presentation.main.adapters.viewHolders.HistoryItemViewHolder

class HistoryItemAdapter: ListAdapter<HistoryItem, HistoryItemViewHolder>(HistoryItemDiffCallback()) {

    var onExprClickListener: ((String) -> Unit)? = null
    var onResClickListener: ((String) -> Unit)? = null
    var formatExpressionCallback: (String) -> String = { it }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        val holder = HistoryItemViewHolder(view)
        holder.exprField.setOnClickListener {
            onExprClickListener?.invoke(holder.exprField.text.toString())
        }
        holder.resField.setOnClickListener {
            onResClickListener?.invoke(holder.resField.text.toString())
        }
        return holder
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.exprField.text = formatExpressionCallback(item.expr)
        holder.resField.text = item.result
        holder.baseField.text = item.base.toString()
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    companion object {
        const val POOL_SIZE = 20
        const val VIEW_TYPE = 1
    }
}