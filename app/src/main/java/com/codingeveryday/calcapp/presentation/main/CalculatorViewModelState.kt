package com.codingeveryday.calcapp.presentation.main

import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.domain.entities.AngleUnit

data class CalculatorViewModelState(
    val expr: String = "",
    val baseColorId: Int = R.color.btnTextColor,
    val angleUnit: AngleUnit = AngleUnit.Radians
)