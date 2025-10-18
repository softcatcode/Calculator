package com.softcat.data.core.interfaces

import com.softcat.domain.entities.AngleUnit
import com.softcat.domain.entities.Number

interface MathInterface {

    fun sum(a: Number, b: Number): Number

    fun sub(a: Number, b: Number): Number

    fun mul(a: Number, b: Number): Number

    fun div(a: Number, b: Number): Number

    fun mod(a: Number, b: Number): Number

    fun pow(a: Number, b: Number): Number

    fun sin(a: Number, angleUnit: AngleUnit = AngleUnit.Radians): Number

    fun cos(a: Number, angleUnit: AngleUnit = AngleUnit.Radians): Number

    fun tan(a: Number, angleUnit: AngleUnit = AngleUnit.Radians): Number

    fun ctg(a: Number, angleUnit: AngleUnit = AngleUnit.Radians): Number

    fun abs(a: Number): Number

    fun fractionPart(a: Number): Number

    fun intPart(a: Number): Number

    fun fac(a: Number): Number

    fun sqrt(a: Number): Number

    fun minus(a: Number): Number

    fun log(a: Number, b: Number): Number

    fun ln(a: Number): Number
}