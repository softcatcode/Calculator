package com.codingeveryday.calcapp.presentation.translation

data class NumberSystemTranslationState(
    val firstBaseError: Boolean = false,
    val secondBaseError: Boolean = false,
    val numberError: Boolean = false,
    val result: String = ""
)