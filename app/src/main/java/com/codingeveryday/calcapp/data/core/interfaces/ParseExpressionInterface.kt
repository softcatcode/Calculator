package com.codingeveryday.calcapp.data.core.interfaces

import com.codingeveryday.calcapp.domain.entities.Expression

interface ParseExpressionInterface {
    fun parseExpression(expr: String, base: Int): Expression
}