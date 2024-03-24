package com.codingeveryday.calcapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.codingeveryday.calcapp.domain.entities.Expression
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
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
    private var historyView: RecyclerView? = null

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
        component.inject(this)
        super.onCreateView(inflater, parent, bundle)
        _binding = FragmentCalculatorBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setTextChangedListeners()
        setObservers()
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setupRecyclerView()
        binding.input.inputType = InputType.TYPE_NULL
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryItemAdapter()
        with (binding.history!!) {
            adapter = historyAdapter
            recycledViewPool.setMaxRecycledViews(HistoryItemAdapter.VIEW_TYPE, HistoryItemAdapter.POOL_SIZE)
        }
        setOnSwipeEvent()
        with (historyAdapter!!) {
            onExprClickListener = {
                calcViewModel.setExpr(it.expr)
            }
            onResClickListener = {
                calcViewModel.addConstant(it.result)
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
                calcViewModel.removeHistoryItem(viewHolder.adapterPosition)
            }
        }
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(historyView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setOnClickListeners() {
        with (binding) {
            launchNSTranslatorBtn.setOnClickListener {
                findNavController().navigate(R.id.action_calculatorFragment_to_toNumberSystemFragment)
            }
            solutionBtn.setOnClickListener {
                val answer = calcViewModel.state.value?.expr ?: ""
                calcViewModel.calculate(base)
                findNavController().navigate(
                    CalculatorFragmentDirections.actionCalculatorFragmentToSolutionViewFragment(
                        calcViewModel.solution, answer
                    )
                )
            }
            equally.setOnClickListener {
                calcViewModel.calculate(base)
            }
            foregroundCalcBtn.setOnClickListener {
                calcViewModel.calculate(base, foregroundMode = true, context = requireActivity().applicationContext)
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
            brackets.setOnClickListener { calcViewModel.openBracket('(') }
            plus.setOnClickListener { calcViewModel.addOperation(CalculationInterface.SUM) }
            sub.setOnClickListener { calcViewModel.addOperation(CalculationInterface.SUB) }
            mul.setOnClickListener { calcViewModel.addOperation(CalculationInterface.MUL) }
            div.setOnClickListener { calcViewModel.addOperation(CalculationInterface.DIV) }
            dot.setOnClickListener { calcViewModel.addPoint() }
            sqrt.setOnClickListener { calcViewModel.addFunction(CalculationInterface.SQRT.toString()) }
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numberSystem!!.setOnClickListener { numberSystem.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black)) }
                absStick!!.setOnClickListener { calcViewModel.addAbsStick() }
                integerPartBrackets!!.setOnClickListener { calcViewModel.openBracket('[') }
                floatPartBrackets!!.setOnClickListener { calcViewModel.openBracket('{') }
                fac!!.setOnClickListener { calcViewModel.addOperation(CalculationInterface.FAC) }
                pow!!.setOnClickListener { calcViewModel.addOperation(CalculationInterface.POW) }
                sin!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.SIN) }
                cos!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.COS) }
                tg!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.TG) }
                ctg!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.CTG) }
                pi!!.setOnClickListener { calcViewModel.addPi() }
                switchRadDeg!!.setOnClickListener {
                    if (switchRadDeg.text == CalculationInterface.RAD)
                        switchRadDeg.text = CalculationInterface.DEG
                    else
                        switchRadDeg.text = CalculationInterface.RAD
                    calcViewModel.switchRadDeg()
                }
                clearHistoryBtn!!.setOnClickListener {
                    calcViewModel.clearHistory()
                }
            }
            input.setOnClickListener {
                input.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
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
        calcViewModel.state.observe(viewLifecycleOwner) {
            historyAdapter?.submitList(it.history)
            binding.input.setText(it.expr)
            val angleLabel = if (it.angleUnit == AngleUnit.Radians)
                CalculationInterface.RAD else CalculationInterface.DEG
            binding.switchRadDeg?.text = angleLabel
            binding.numberSystem?.setTextColor(it.baseColor)
        }
    }

}