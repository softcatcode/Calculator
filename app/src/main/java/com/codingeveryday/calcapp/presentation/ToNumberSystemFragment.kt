package com.codingeveryday.calcapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.databinding.FragmentToNumberSystemBinding
import javax.inject.Inject

class ToNumberSystemFragment: Fragment() {

    private var _binding: FragmentToNumberSystemBinding? = null
    val binding: FragmentToNumberSystemBinding
        get() = _binding ?: throw RuntimeException("toNSTranslator's binding is null")

    private val component by lazy {
        (requireActivity().application as CalculatorApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ConvertNumberSystemViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        component.inject(this)
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentToNumberSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setObservers()
    }

    private fun setOnClickListeners() {
        with (binding) {
            btnConvert.setOnClickListener {
                viewModel.translate(
                    numberField.text.toString(),
                    initBase.text.toString(),
                    transBase.text.toString()
                )
            }
            numberField.setOnClickListener {
                numberField.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            }
            transBase.setOnClickListener {
                transBase.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            }
            initBase.setOnClickListener {
                initBase.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            }
            btnSwap.setOnClickListener {
                val a = initBase.text
                val b = transBase.text
                initBase.text = b
                transBase.text = a
                initBase.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                transBase.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnClear.setOnClickListener {
                numberField.setText("")
            }
        }
    }

    private fun setObservers() {
        with (viewModel) {
            numberCorrect.observe(viewLifecycleOwner) {
                binding.numberField.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red))
            }
            number.observe(viewLifecycleOwner) {
                binding.answer.text = it
            }
            baseSourceCorrect.observe(viewLifecycleOwner) {
                binding.initBase.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red))
            }
            baseDestCorrect.observe(viewLifecycleOwner) {
                binding.transBase.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red))
            }
        }
    }
}