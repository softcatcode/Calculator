package com.codingeveryday.calcapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codingeveryday.calcapp.databinding.FragmentSolutionViewBinding

class SolutionViewFragment: Fragment() {

    private val args by navArgs<SolutionViewFragmentArgs>()

    private var _binding: FragmentSolutionViewBinding? = null
    private val binding: FragmentSolutionViewBinding
        get() = _binding ?:throw RuntimeException("solution view binding is null")

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, bundle: Bundle?): View {
        super.onCreateView(inflater, parent, bundle)
        _binding = FragmentSolutionViewBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        binding.output.text = args.solution
        binding.answerField.text = args.answer
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
    }
}