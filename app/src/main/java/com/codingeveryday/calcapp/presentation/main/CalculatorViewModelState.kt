package com.codingeveryday.calcapp.presentation.main

import android.graphics.Color
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.HistoryItem

data class CalculatorViewModelState(
    val expr: String = "",
    val baseColor: Int = Color.BLACK,
    val angleUnit: AngleUnit = AngleUnit.Radians
)