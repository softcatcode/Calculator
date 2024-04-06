package com.codingeveryday.calcapp.data.states

data class NumberSystemTranslationState(
    val firstBaseError: Boolean = false,
    val secondBaseError: Boolean = false,
    val numberError: Boolean = false,
    val result: String = ""
)