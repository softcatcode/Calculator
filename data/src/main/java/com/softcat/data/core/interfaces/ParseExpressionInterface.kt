package com.softcat.data.core.interfaces

import com.softcat.domain.entities.Expression

interface ParseExpressionInterface {
    fun parseExpression(expr: String, base: Int): Expression
}