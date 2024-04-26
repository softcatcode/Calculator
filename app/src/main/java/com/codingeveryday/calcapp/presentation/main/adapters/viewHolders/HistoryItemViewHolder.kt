package com.codingeveryday.calcapp.presentation.main.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.codingeveryday.calcapp.R

class HistoryItemViewHolder(view: View): ViewHolder(view) {
    val exprField: TextView = view.findViewById(R.id.expr)
    val resField: TextView = view.findViewById(R.id.result)
    val baseField: TextView = view.findViewById(R.id.base)
}