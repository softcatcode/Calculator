package com.codingeveryday.calcapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.presentation.compose.NumberSystemTranslatorDesign
import javax.inject.Inject

class ToNumberSystemFragment: Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_to_number_systems_jc, container, false)
        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent{
                NumberSystemTranslatorDesign(
                    viewModel = viewModel,
                    onBackPressed = { findNavController().popBackStack() }
                )
            }
        }
        return view
    }
}
