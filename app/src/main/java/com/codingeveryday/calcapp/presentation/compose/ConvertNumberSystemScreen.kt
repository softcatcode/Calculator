package com.codingeveryday.calcapp.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.presentation.ConvertNumberSystemViewModel

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
        Text(
            stringResource(id = R.string.direction),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.4f)
                .padding(20.dp),
            fontSize = 20.sp
        )
        TextField(
            value = firstBase,
            onValueChange = onFirstBaseChanged,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 30.sp),
            modifier = Modifier
                .fillMaxWidth(0.333f)
                .fillMaxHeight()
                .background(Color.White)
                .padding(5.dp),
        )
        IconButton(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxHeight()
                .fillMaxWidth(0.4f)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
            onClick = onTranslationDirChanged
        ) {
            val painter = if (direction)
                painterResource(id = R.drawable.pointer_right)
            else
                painterResource(id = R.drawable.pointer_left)
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(0.7f),
            )
        }
        TextField(
            value = secondBase,
            onValueChange = onSecondBaseChanged,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 30.sp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(5.dp),
        )
    }
}

@Composable
private fun TranslationInputField(
    modifier: Modifier = Modifier,
    number: String = "",
    onValueChanged: (String) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = number,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 20.sp),
            onValueChange = onValueChanged,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            placeholder = {
                Text("number")
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = {}
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.backspace),
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
fun NumberSystemTranslatorDesign(viewModel: ConvertNumberSystemViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxHeight(0.05f)
                .fillMaxWidth(0.15f)
                .background(color = Color.Green, shape = RoundedCornerShape(10.dp)),
            onClick = {  }
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
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.teal_200))
        ) {
            Text(stringResource(id = R.string.transform_number_label))
        }
        var offset by remember { mutableStateOf(0f) }
        Text(
            text = state.result,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp)
                .scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                    }
                )
        )
    }
}