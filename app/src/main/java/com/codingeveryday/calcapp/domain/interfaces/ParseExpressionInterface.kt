package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.domain.entities.Expression

interface ParseExpressionInterface {
    fun parseExpression(expr: String, base: Int): Expression
}