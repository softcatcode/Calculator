package com.codingeveryday.calcapp.presentation.translation

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingeveryday.calcapp.R

@Composable
private fun BaseInputField(
    modifier: Modifier = Modifier,
    base: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = base,
        onValueChange = onValueChanged,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        ),
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = colorResource(id = R.color.lightBlueColor),
            focusedIndicatorColor = colorResource(id = R.color.lightBlueColor)
        ),
        shape = CircleShape,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun TranslationSetupPanel(
    modifier: Modifier = Modifier,
    firstBase: String = "10",
    secondBase: String = "10",
    direction: Boolean = true,
    onTranslationDirChanged: () -> Unit = {},
    onFirstBaseChanged: (String) -> Unit = {},
    onSecondBaseChanged: (String) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(id = R.string.direction),
                fontSize = 20.sp
            )
        }
        BaseInputField(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            base = firstBase,
            onValueChanged = onFirstBaseChanged
        )
        IconButton(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxHeight()
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .weight(1f),
            onClick = onTranslationDirChanged
        ) {
            val painter = if (direction)
                painterResource(id = R.drawable.pointer_right)
            else
                painterResource(id = R.drawable.pointer_left)
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
            )
        }
        BaseInputField(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            base = secondBase,
            onValueChanged = onSecondBaseChanged
        )
    }
}

@Composable
private fun TranslationInputField(
    modifier: Modifier = Modifier,
    number: String = "",
    onValueChanged: (String) -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = number,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start, fontSize = 25.sp),
            onValueChange = onValueChanged,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = colorResource(id = R.color.lightBlueColor),
                unfocusedIndicatorColor = colorResource(id = R.color.lightBlueColor),
                focusedContainerColor = colorResource(id = R.color.inputFieldColor),
                unfocusedContainerColor = colorResource(id = R.color.inputFieldColor)
            )
        )
    }
}

@Composable
fun ScrollableResultView(modifier: Modifier = Modifier, state: NumberSystemTranslationState) {
    var offset by remember { mutableFloatStateOf(0f) }
    Box(
        modifier = modifier.scrollable(
            orientation = Orientation.Vertical,
            state = rememberScrollableState { delta ->
                offset += delta.toInt()
                delta
            }
        )
    ) {
        Text(
            text = state.result,
            fontSize = 30.sp,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = ScrollState(offset.toInt()))
        )
    }
}

@Composable
fun NumberSystemTranslatorDesign(
    viewModel: ConvertNumberSystemViewModel,
    onBackPressed: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxHeight(0.06f)
                .fillMaxWidth(0.2f)
                .background(
                    color = colorResource(id = R.color.lightBlueColor),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            onClick = { onBackPressed() },
        ) {
            Image(
                painter = painterResource(id = R.drawable.pointer_left),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }
        TranslationSetupPanel(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            firstBase = viewModel.firstBase,
            secondBase = viewModel.secondBase,
            direction = viewModel.translationDir,
            onTranslationDirChanged = { viewModel.switchTranslationDir() },
            onFirstBaseChanged = { viewModel.updateFirstBase(it) },
            onSecondBaseChanged = { viewModel.updateSecondBase(it) }
        )
        TranslationInputField(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            number = viewModel.number,
            onValueChanged = { viewModel.updateNumber(it) }
        )
        Button(
            onClick = { viewModel.translate() },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.translationButtonColor)
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.transform_number_label),
                fontSize = 30.sp
            )
        }
        ScrollableResultView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp)
        )
    }
}