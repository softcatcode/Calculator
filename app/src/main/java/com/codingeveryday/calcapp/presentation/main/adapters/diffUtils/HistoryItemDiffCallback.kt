package com.codingeveryday.calcapp.presentation.main.adapters.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.softcat.domain.entities.HistoryItem

class HistoryItemDiffCallback: DiffUtil.ItemCallback<HistoryItem>() {
    override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
        return oldItem == newItem
    }
}