package com.codingeveryday.calcapp.domain

data class HistoryItem(
    val expr: String,
    val result: String,
    val id: Int = DEFAULT_ID
) {
    companion object {
        const val DEFAULT_ID = 0
    }
}