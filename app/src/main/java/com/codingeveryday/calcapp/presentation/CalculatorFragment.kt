package com.codingeveryday.calcapp.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.util.Log
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
import com.codingeveryday.calcapp.data.CalcService
import com.codingeveryday.calcapp.databinding.FragmentCalculatorBinding
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface
import javax.inject.Inject

class CalculatorFragment: Fragment() {

    private var solution = ""
    private var keyboardOn = false
    private var numberSystemData = "10"
    private var historyAdapter: HistoryItemAdapter? = null
    private var historyListView: RecyclerView? = null
    private var historyUpdate = true

    private val component by lazy {
        (requireActivity().application as CalculatorApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val calcViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CalculatorViewModel::class.java]
    }
    private val historyViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HistoryViewModel::class.java]
    }

    private var _binding: FragmentCalculatorBinding? = null
    val binding: FragmentCalculatorBinding
        get() = _binding ?: throw RuntimeException("calculator's binding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("mumu", "onAttach")
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, bundle: Bundle?): View {
        component.inject(this)
        super.onCreateView(inflater, parent, bundle)
        Log.i("mumu", "onCreateView")
        _binding = FragmentCalculatorBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("mumu", "onViewCreated")
        setOnClickListeners()
        setTextChangedListeners()
        setObservers()
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setupRecyclerView()
        binding.input.inputType = InputType.TYPE_NULL
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryItemAdapter()
        historyListView = binding.history!!
        with (historyListView!!) {
            adapter = historyAdapter
            recycledViewPool.setMaxRecycledViews(HistoryItemAdapter.VIEW_TYPE, HistoryItemAdapter.POOL_SIZE)
        }
        setOnSwipeEvent()
        with (historyAdapter!!) {
            onExprClickListener = {
                calcViewModel.setExpr(it.expr)
            }
            onResClickListener = {
                calcViewModel.setExpr((calcViewModel.expr.value ?: "") + it.result)
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
                historyViewModel.remove(historyAdapter!!.currentList[viewHolder.adapterPosition].id)
            }
        }
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(historyListView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("mumu", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i("mumu", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("mumu", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("mumu", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("mumu", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("mumu", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.i("mumu", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("mumu", "onDetach")
    }

    private fun setOnClickListeners() {
        with (binding) {
            launchNSTranslatorBtn.setOnClickListener {
                findNavController().navigate(R.id.action_calculatorFragment_to_toNumberSystemFragment)
            }
            solutionBtn.setOnClickListener {
                val tmp = calcViewModel.expr.value ?: ""
                calcViewModel.calculate(numberSystemData)
                if (historyUpdate)
                    historyViewModel.add(tmp, calcViewModel.expr.value.toString())

                input.setText(tmp)
                findNavController().navigate(
                    CalculatorFragmentDirections.actionCalculatorFragmentToSolutionViewFragment(
                        solution,
                        if (historyUpdate) calcViewModel.expr.value.toString() else ""
                    )
                )
                historyUpdate = true
            }
            equally.setOnClickListener {
                val tmp = calcViewModel.expr.value.toString()
                calcViewModel.calculate(numberSystemData)
                if (historyUpdate)
                    historyViewModel.add(tmp, calcViewModel.expr.value.toString())
                historyUpdate = true
            }
            foregroundCalcBtn.setOnClickListener {
                val activity = requireActivity() as MainActivity
                if (!activity.checkNotificationPermission()) {
                    activity.requestNotificationPermission()
                    return@setOnClickListener
                }
                if (CalcService.running)
                    return@setOnClickListener
                val context = requireActivity().applicationContext
                if (numberSystemData.isEmpty() || !numberSystemData.isDigitsOnly()) {
                    Toast.makeText(context, getString(R.string.number_system_error), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val base = numberSystemData.toInt()
                if (base < 2 || base > 36) {
                    Toast.makeText(context, getString(R.string.number_system_error), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val expr = calcViewModel.expr.value ?: ""
                ContextCompat.startForegroundService(context, CalcService.newIntent(context, expr, base))
                return@setOnClickListener
            }

            backspace.setOnClickListener { calcViewModel.popBack() }
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
            plus.setOnClickListener { calcViewModel.addOperation('+') }
            sub.setOnClickListener { calcViewModel.addOperation('-') }
            mul.setOnClickListener { calcViewModel.addOperation('×') }
            div.setOnClickListener { calcViewModel.addOperation('/') }
            dot.setOnClickListener { calcViewModel.addDot() }
            sqrt.setOnClickListener { calcViewModel.addFunction(CalculationInterface.SQRT_ID) }
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numberSystem!!.setOnClickListener { numberSystem.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black)) }
                absStick!!.setOnClickListener { calcViewModel.addAbsStick() }
                integerPartBrackets!!.setOnClickListener { calcViewModel.openBracket('[') }
                floatPartBrackets!!.setOnClickListener { calcViewModel.openBracket('{') }
                fac!!.setOnClickListener { calcViewModel.addOperation('!') }
                pow!!.setOnClickListener { calcViewModel.addOperation('^') }
                sin!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.SIN_ID) }
                cos!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.COS_ID) }
                tg!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.TG_ID) }
                ctg!!.setOnClickListener { calcViewModel.addFunction(CalculationInterface.CTG_ID) }
                pi!!.setOnClickListener { calcViewModel.addPi() }
                switchRadDeg!!.setOnClickListener {
                    if (switchRadDeg.text == "rad")
                        switchRadDeg.text = "deg"
                    else
                        switchRadDeg.text = "rad"
                    calcViewModel.switchRadDeg()
                }
                clearHistoryBtn!!.setOnClickListener {
                    historyViewModel.clear()
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
        binding.numberSystem!!.addTextChangedListener {
            if (it!!.length > 2)
                it.delete(2, 3)
            numberSystemData = it.toString()
            var base = 0
            if (numberSystemData.isNotEmpty() && numberSystemData.isDigitsOnly())
                base = numberSystemData.toInt()
            setActiveDigitButtons(base)
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
        historyViewModel.historyItemList.observe(viewLifecycleOwner) {
            historyAdapter?.submitList(it)
        }
        calcViewModel.solution.observe(viewLifecycleOwner) {
            solution = it
        }
        calcViewModel.expr.observe(viewLifecycleOwner) {
            binding.input.setText(it)
        }
        calcViewModel.baseCorrect.observe(viewLifecycleOwner) {
            if (calcViewModel.baseCorrect.value == true)
                return@observe
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                binding.numberSystem!!.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red))
            historyUpdate = false
            Toast.makeText(requireActivity(), getString(R.string.number_system_error), Toast.LENGTH_SHORT).show()
        }
        calcViewModel.exprCorrect.observe(viewLifecycleOwner) {
            if (calcViewModel.exprCorrect.value == true)
                return@observe
            Toast.makeText(requireActivity(), getString(R.string.expression_error), Toast.LENGTH_SHORT).show()
            historyUpdate = false
            calcViewModel.resetErrors()
        }
    }

}