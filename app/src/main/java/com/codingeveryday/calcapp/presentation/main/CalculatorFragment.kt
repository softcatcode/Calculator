package com.codingeveryday.calcapp.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.databinding.FragmentCalculatorBinding
import com.codingeveryday.calcapp.domain.entities.AngleUnit
import com.codingeveryday.calcapp.domain.entities.BracketType
import com.codingeveryday.calcapp.domain.entities.closingBracket
import com.codingeveryday.calcapp.domain.entities.openingBracket
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.COS
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.CTG
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.DEG
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.DIV
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.FAC
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.MUL
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.PI
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.POW
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.RAD
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SIN
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SQRT
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SUB
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.SUM
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.LN
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface.Companion.TAN
import com.codingeveryday.calcapp.presentation.main.adapters.HistoryItemAdapter
import com.codingeveryday.calcapp.presentation.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class CalculatorFragment: Fragment() {

    private val base: String
        get() {
            return if (binding.numberSystem == null)
                "10"
            else
                binding.numberSystem?.text.toString()
        }

    private var historyAdapter: HistoryItemAdapter? = null

    private val component by lazy {
        (requireActivity().application as CalculatorApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val calcViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CalculatorViewModel::class.java]
    }

    private var _binding: FragmentCalculatorBinding? = null
    private val binding: FragmentCalculatorBinding
        get() = _binding ?: throw RuntimeException("calculator's binding is null")

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, bundle: Bundle?): View {
        Timber.i("${this::class.simpleName}.onCreateView")
        component.inject(this)
        super.onCreateView(inflater, parent, bundle)
        _binding = FragmentCalculatorBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("${this::class.simpleName}.onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setTextChangedListeners()
        setObservers()
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setupRecyclerView()
    }

    override fun onResume() {
        Timber.i("${this::class.simpleName}.onResume")
        super.onResume()
        calcViewModel.updateExpression()
    }

    override fun onDestroyView() {
        Timber.i("${this::class.simpleName}.onDestroyView()")
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryItemAdapter()
        with (binding.history!!) {
            adapter = historyAdapter
            recycledViewPool.setMaxRecycledViews(
                HistoryItemAdapter.VIEW_TYPE,
                HistoryItemAdapter.POOL_SIZE
            )
        }
        setOnSwipeEvent()
        with (historyAdapter!!) {
            onExprClickListener = {
                calcViewModel.setExpr(it)
            }
            onResClickListener = {
                calcViewModel.addConstant(it)
            }
            formatExpressionCallback = {
                formatExpression(it)
            }
        }
    }

    private fun setOnSwipeEvent() {
        val callback = object: ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Timber.i("History item is swiped.")
                calcViewModel.removeHistoryItem(viewHolder.adapterPosition)
            }
        }
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.history)
    }

    private fun setOnClickListeners() {
        with (binding) {
            launchNSTranslatorBtn.setOnClickListener {
                findNavController().navigate(R.id.action_calculatorFragment_to_toNumberSystemFragment)
            }
            equally.setOnClickListener {
                calcViewModel.calculate(base)
            }
            equally.setOnLongClickListener {
                calcViewModel.calculate(base, foregroundMode = true, context = requireActivity().applicationContext)
                true
            }
            keyboard.setOnClickListener {
                findNavController().navigate(R.id.action_calculatorFragment_to_keyboardDialog)
            }

            backspace.setOnClickListener { calcViewModel.backspace() }
            clear.setOnClickListener { calcViewModel.clear() }
            zero.setOnClickListener { calcViewModel.appendDigit('0') }
            one.setOnClickListener { calcViewModel.appendDigit('1') }
            two.setOnClickListener { calcViewModel.appendDigit('2') }
            three.setOnClickListener { calcViewModel.appendDigit('3') }
            four.setOnClickListener { calcViewModel.appendDigit('4') }
            five.setOnClickListener { calcViewModel.appendDigit('5') }
            six.setOnClickListener { calcViewModel.appendDigit('6') }
            seven.setOnClickListener { calcViewModel.appendDigit('7') }
            eight.setOnClickListener { calcViewModel.appendDigit('8') }
            nine.setOnClickListener { calcViewModel.appendDigit('9') }
            brackets.setOnClickListener { calcViewModel.openBracket(BracketType.Round) }
            plus.setOnClickListener { calcViewModel.addOperation(SUM) }
            sub.setOnClickListener { calcViewModel.addOperation(SUB) }
            mul.setOnClickListener { calcViewModel.addOperation(MUL) }
            div.setOnClickListener { calcViewModel.addOperation(DIV) }
            dot.setOnClickListener { calcViewModel.addPoint() }
            sqrt.setOnClickListener { calcViewModel.addFunction(SQRT.toString()) }
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numberSystem!!.setOnClickListener { numberSystem.setTextColor(ContextCompat.getColor(requireActivity(), R.color.btnTextColor)) }
                absStick!!.setOnClickListener { calcViewModel.addAbsStick() }
                integerPartBrackets!!.setOnClickListener { calcViewModel.openBracket(BracketType.Square) }
                floatPartBrackets!!.setOnClickListener { calcViewModel.openBracket(BracketType.Curly) }
                fac!!.setOnClickListener { calcViewModel.addOperation(FAC) }
                pow!!.setOnClickListener { calcViewModel.addOperation(POW) }
                sin!!.setOnClickListener { calcViewModel.addFunction(SIN) }
                cos!!.setOnClickListener { calcViewModel.addFunction(COS) }
                tg!!.setOnClickListener { calcViewModel.addFunction(TAN) }
                ctg!!.setOnClickListener { calcViewModel.addFunction(CTG) }
                ln!!.setOnClickListener { calcViewModel.addFunction(LN) }
                pi!!.setOnClickListener { calcViewModel.addConstant(PI.toString()) }
                switchRadDeg!!.setOnClickListener { calcViewModel.switchRadDeg() }
                clearHistoryBtn!!.setOnClickListener { calcViewModel.clearHistory() }
            }
            sendLogs.setOnClickListener {
                calcViewModel.sendLogs()
            }
        }
    }

    private fun setTextChangedListeners() {
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            return
        binding.numberSystem?.addTextChangedListener {
            if (it!!.length > 2)
                it.delete(2, 3)
            if (base.isNotEmpty() && base.isDigitsOnly())
                setActiveDigitButtons(base.toInt())
        }
    }

    private fun setActiveDigitButtons(base: Int) {
        Timber.i("${this::class.simpleName}.setActiveDigitButtons($base)")
        binding.zero.isClickable = base > 0
        binding.one.isClickable = base > 1
        binding.two.isClickable = base > 2
        binding.three.isClickable = base > 3
        binding.four.isClickable = base > 4
        binding.five.isClickable = base > 5
        binding.six.isClickable = base > 6
        binding.seven.isClickable = base > 7
        binding.eight.isClickable = base > 8
        binding.nine.isClickable = base > 9
    }

    private fun setObservers() {
        calcViewModel.history.observe(viewLifecycleOwner) {
            Timber.i("calcViewModel.history observer")
            historyAdapter?.submitList(it)
        }
        calcViewModel.state.observe(viewLifecycleOwner) {
            Timber.i("calcViewModel.state observer")
            binding.input.text = formatExpression(it.expr)
            val angleLabel = if (it.angleUnit == AngleUnit.Radians) RAD else DEG
            binding.switchRadDeg?.text = angleLabel
            binding.numberSystem?.setTextColor(ContextCompat.getColor(requireActivity(), it.baseColorId))
        }
        calcViewModel.errorEvent.observe(viewLifecycleOwner) {
            Timber.i("calcViewModel.errorEvent observer")
            if (it.isNotEmpty()) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                calcViewModel.resetError()
            }
        }
    }

    private fun formatExpression(s: String): String {
        Timber.i("${this::class.simpleName}.formatExpression($s)")
        val openAbs = openingBracket(BracketType.Triangle)
        val closeAbs = closingBracket(BracketType.Triangle)
        val sb = StringBuilder()
        for (c in s) {
            if (c == openAbs || c == closeAbs)
                sb.append(CalculationInterface.ABS)
            else
                sb.append(c)
        }
        return sb.toString()
    }
}