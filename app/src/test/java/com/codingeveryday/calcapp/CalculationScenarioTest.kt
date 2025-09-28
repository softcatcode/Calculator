package com.codingeveryday.calcapp

import android.os.Looper.getMainLooper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.codingeveryday.calcapp.presentation.main.MainActivity
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import kotlin.use

@RunWith(RobolectricTestRunner::class)
class CalculationScenarioTest {

    companion object {
        private const val CALCULATION_TIMEOUT = 100L
    }

    private lateinit var buttons: MutableMap<Char, Button>
    private lateinit var imageButtons: MutableMap<Char, ImageButton>
    private lateinit var funcButtons: MutableMap<String, Button>

    @Before
    fun init() {
        buttons = mutableMapOf()
        imageButtons = mutableMapOf()
        funcButtons = mutableMapOf()
    }

    private fun setupButtons(activity: MainActivity) {
        buttons['1'] = activity.findViewById<Button>(R.id.one)
        buttons['2'] = activity.findViewById<Button>(R.id.two)
        buttons['3'] = activity.findViewById<Button>(R.id.three)
        buttons['4'] = activity.findViewById<Button>(R.id.four)
        buttons['5'] = activity.findViewById<Button>(R.id.five)
        buttons['6'] = activity.findViewById<Button>(R.id.six)
        buttons['7'] = activity.findViewById<Button>(R.id.seven)
        buttons['8'] = activity.findViewById<Button>(R.id.eight)
        buttons['9'] = activity.findViewById<Button>(R.id.nine)
        buttons['0'] = activity.findViewById<Button>(R.id.zero)
        buttons['A'] = activity.findViewById<Button>(R.id.a)
        buttons['B'] = activity.findViewById<Button>(R.id.b)
        buttons['C'] = activity.findViewById<Button>(R.id.c)
        buttons['D'] = activity.findViewById<Button>(R.id.d)
        buttons['E'] = activity.findViewById<Button>(R.id.e)
        buttons['F'] = activity.findViewById<Button>(R.id.f)
        buttons['+'] = activity.findViewById<Button>(R.id.plus)
        buttons['-'] = activity.findViewById<Button>(R.id.sub)
        buttons['^'] = activity.findViewById<Button>(R.id.pow)
        buttons['/'] = activity.findViewById<Button>(R.id.div)
        buttons['*'] = activity.findViewById<Button>(R.id.mul)
        buttons['.'] = activity.findViewById<Button>(R.id.dot)
        buttons['!'] = activity.findViewById<Button>(R.id.fac)
        buttons['('] = activity.findViewById<Button>(R.id.brackets)
        buttons['|'] = activity.findViewById<Button>(R.id.absStick)
        imageButtons['p'] = activity.findViewById<ImageButton>(R.id.pi)
        imageButtons['='] = activity.findViewById<ImageButton>(R.id.equally)
        imageButtons['#'] = activity.findViewById<ImageButton>(R.id.keyboard)
        funcButtons["sin"] = activity.findViewById<Button>(R.id.sin)
        funcButtons["cos"] = activity.findViewById<Button>(R.id.cos)
        funcButtons["tg"] = activity.findViewById<Button>(R.id.tg)
        funcButtons["ctg"] = activity.findViewById<Button>(R.id.ctg)
        funcButtons["ln"] = activity.findViewById<Button>(R.id.ln)
        funcButtons["sqrt"] = activity.findViewById<Button>(R.id.sqrt)
    }

    @Test
    fun calculateElementaryExpr() = runTest {
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            val activity = controller.get()
            setupButtons(activity)
            buttons['1']?.performClick()
            buttons['2']?.performClick()
            buttons['-']?.performClick()
            buttons['1']?.performClick()
            buttons['0']?.performClick()
            buttons['+']?.performClick()
            buttons['.']?.performClick()
            buttons['7']?.performClick()
            val expr = activity.findViewById<TextView>(R.id.input).text.toString()
            imageButtons['=']?.performClick()
            while (activity.findViewById<TextView>(R.id.input).text.toString() == expr) {
                delay(CALCULATION_TIMEOUT)
                shadowOf(getMainLooper()).idle()
            }
            val result = activity.findViewById<TextView>(R.id.input).text.toString()
            activity.finish()

            TestCase.assertEquals(expr, "12-10+0.7")
            TestCase.assertEquals(result, "2.7")
        }
    }

    @Test
    fun calculateTrigExpr() = runTest {
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            RuntimeEnvironment.setQualifiers("+land")
            controller.configurationChange()
            val activity = controller.get()
            setupButtons(activity)
            funcButtons["sin"]?.performClick()
            funcButtons["cos"]?.performClick()
            imageButtons['p']?.performClick()
            buttons['(']?.performClick()
            imageButtons['p']?.performClick()
            buttons['(']?.performClick()
            val expr = activity.findViewById<TextView>(R.id.input).text.toString()
            imageButtons['=']?.performClick()
            while (activity.findViewById<TextView>(R.id.input).text.toString() == expr) {
                delay(CALCULATION_TIMEOUT)
                shadowOf(getMainLooper()).idle()
            }
            val result = activity.findViewById<TextView>(R.id.input).text.toString()
            activity.finish()

            TestCase.assertEquals(expr, "sin(cos(π)×π)")
            TestCase.assertEquals(result, "0")
        }
    }

    @Test
    fun calculateNestedAbsExpr() = runTest {
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            RuntimeEnvironment.setQualifiers("+land")
            controller.configurationChange()
            val activity = controller.get()
            setupButtons(activity)
            buttons['|']?.performClick()
            buttons['|']?.performClick()
            buttons['1']?.performClick()
            buttons['/']?.performClick()
            buttons['2']?.performClick()
            buttons['-']?.performClick()
            buttons['3']?.performClick()
            buttons['!']?.performClick()
            buttons['!']?.performClick()
            buttons['|']?.performClick()
            buttons['-']?.performClick()
            buttons['|']?.performClick()
            buttons['3']?.performClick()
            buttons['^']?.performClick()
            buttons['2']?.performClick()
            buttons['-']?.performClick()
            funcButtons["sqrt"]?.performClick()
            buttons['1']?.performClick()
            buttons['2']?.performClick()
            buttons['1']?.performClick()
            buttons['(']?.performClick()
            buttons['|']?.performClick()
            buttons['|']?.performClick()
            val expr = activity.findViewById<TextView>(R.id.input).text.toString()
            imageButtons['=']?.performClick()
            while (activity.findViewById<TextView>(R.id.input).text.toString() == expr) {
                delay(CALCULATION_TIMEOUT)
                shadowOf(getMainLooper()).idle()
            }
            val result = activity.findViewById<TextView>(R.id.input).text.toString()
            activity.finish()

            TestCase.assertEquals(expr, "||1/2-3!|-|3^2-√(121)||")
            TestCase.assertEquals(result, "3.5")
        }
    }

    @Test
    fun calculateBinaryExpr() = runTest {
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            RuntimeEnvironment.setQualifiers("+land")
            controller.configurationChange()
            val activity = controller.get()
            activity.findViewById<EditText>(R.id.number_system).setText("2")
            setupButtons(activity)
            buttons['1']?.performClick()
            buttons['1']?.performClick()
            buttons['0']?.performClick()
            buttons['+']?.performClick()
            buttons['1']?.performClick()
            buttons['0']?.performClick()
            buttons['0']?.performClick()
            buttons['1']?.performClick()
            buttons['*']?.performClick()
            buttons['(']?.performClick()
            buttons['1']?.performClick()
            buttons['-']?.performClick()
            buttons['1']?.performClick()
            buttons['1']?.performClick()
            buttons['0']?.performClick()
            buttons['(']?.performClick()
            val expr = activity.findViewById<TextView>(R.id.input).text.toString()
            imageButtons['=']?.performClick()
            while (activity.findViewById<TextView>(R.id.input).text.toString() == expr) {
                delay(CALCULATION_TIMEOUT)
                shadowOf(getMainLooper()).idle()
            }
            val result = activity.findViewById<TextView>(R.id.input).text.toString()
            activity.finish()

            TestCase.assertEquals(expr, "110+1001×(1-110)")
            TestCase.assertEquals(result, "-100111")
        }
    }

    @Test
    fun calculateHexExpr() = runTest {
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            RuntimeEnvironment.setQualifiers("+land")
            controller.configurationChange()
            controller.get().findViewById<EditText>(R.id.number_system).setText("16")
            RuntimeEnvironment.setQualifiers("+port")
            controller.configurationChange()
            val activity = controller.get()
            setupButtons(activity)
            buttons['1']?.performClick()
            buttons['0']?.performClick()
            buttons['-']?.performClick()
            buttons['5']?.performClick()
            buttons['+']?.performClick()
            buttons['1']?.performClick()
            buttons['2']?.performClick()
            val expr = activity.findViewById<TextView>(R.id.input).text.toString()
            imageButtons['=']?.performClick()
            while (activity.findViewById<TextView>(R.id.input).text.toString() == expr) {
                delay(CALCULATION_TIMEOUT)
                shadowOf(getMainLooper()).idle()
            }
            val result = activity.findViewById<TextView>(R.id.input).text.toString()
            activity.finish()

            TestCase.assertEquals(expr, "10-5+12")
            TestCase.assertEquals(result, "1D")
        }
    }
}