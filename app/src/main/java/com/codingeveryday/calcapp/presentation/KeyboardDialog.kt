package com.codingeveryday.calcapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingeveryday.calcapp.CalculatorApplication
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.presentation.compose.KeyboardFragmentDesign
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class KeyboardDialog: BottomSheetDialogFragment() {

    val onOkClicked: () -> Unit = {}

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
        onOkClicked()
        dismiss()
    }
}