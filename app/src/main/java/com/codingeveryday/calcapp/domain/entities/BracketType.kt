package com.codingeveryday.calcapp.domain.entities

enum class BracketType {
    Round,
    Square,
    Curly,
    Triangle
}

fun openingBracket(type: BracketType) = when (type) {
    BracketType.Round -> '('
    BracketType.Curly -> '{'
    BracketType.Square -> '['
    BracketType.Triangle -> '<'
}

fun closingBracket(type: BracketType) = when (type) {
    BracketType.Round -> ')'
    BracketType.Curly -> '}'
    BracketType.Square -> ']'
    BracketType.Triangle -> '>'
}

fun bracketType(bracket: Char) = when (bracket) {
    in "()" -> BracketType.Round
    in "[]" -> BracketType.Square
    in "{}" -> BracketType.Curly
    in "<>" -> BracketType.Triangle
    else -> null
}