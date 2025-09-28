package com.codingeveryday.calcapp.presentation.keyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.presentation.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class KeyboardDialog: Fragment() {

    private val component by lazy {
        (requireActivity().application as CalculatorApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[KeyboardFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("${this::class.simpleName}.onCreateView")
        component.inject(this)
        val view = inflater.inflate(R.layout.fragment_keyboard_dialog, container, false)
        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                KeyboardFragmentDesign(viewModel, ::onClose)
            }
        }
        return view
    }

    private fun onClose() {
        Timber.i("${this::class.simpleName}.onClose()")
        findNavController().popBackStack()
    }
}