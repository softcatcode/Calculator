package com.codingeveryday.calcapp.domain.entities

data class HistoryItem(
    val expr: String,
    val result: String,
    val base: Int,
    val id: Int = DEFAULT_ID
) {
    companion object {
        const val DEFAULT_ID = 0
    }
}