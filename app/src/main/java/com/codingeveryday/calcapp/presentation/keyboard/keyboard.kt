package com.codingeveryday.calcapp.presentation.keyboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingeveryday.calcapp.R
import com.softcat.domain.entities.Expression.Companion.DIGITS
import kotlin.math.min

@Composable
fun ButtonRow(
    modifier: Modifier,
    labelList: List<String>,
    onClick: (String) -> Unit
) {
    Row(modifier) {
        Spacer(modifier =  Modifier.weight(1f))
        labelList.forEachIndexed { index, item ->
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f / (labelList.size - index + 2)),
                onClick = { onClick(item) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = colorResource(id = R.color.digitButtonColor),
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun Keyboard(
    characters: String,
    charactersInRow: Int,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Column(modifier) {
        var i = 0
        Spacer(modifier = Modifier.weight(1f))
        while (i < characters.length) {
            val labels = characters.substring(i, min(i + charactersInRow, characters.length))
                .toCharArray().map { "$it" }
            ButtonRow(
                modifier = Modifier.fillMaxWidth().weight(3f),
                labelList = labels,
                onClick = onClick
            )
            Spacer(modifier = Modifier.weight(1f))
            i += charactersInRow
        }
    }
}

@Composable
fun InputView(
    modifier: Modifier = Modifier,
    text: String,
    onBackspaceClicked: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors().copy(containerColor = Color.White),
        border = BorderStroke(2.dp, colorResource(id = R.color.purple_500))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 5.dp),
                text = text,
                color = Color.Black,
                fontSize = 50.sp,
            )
            Icon(
                modifier = Modifier
                    .width(50.dp)
                    .padding(end = 5.dp)
                    .clickable { onBackspaceClicked() },
                contentDescription = null,
                painter = painterResource(id = R.drawable.backspace),
                tint = Color.Black
            )
        }

    }
}

@Composable
fun KeyboardScreen(
    text: String = "",
    onBackspaceClicked: () -> Unit = {},
    onDigitClick: (Char) -> Unit = {},
    onOkClicked: () -> Unit = {},
    paddings: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .padding(paddings)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(colorResource(id = R.color.calculatorBackgroundColor))
            .padding(5.dp)
    ) {
        InputView(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(Color.White),
            text = text,
            onBackspaceClicked = onBackspaceClicked
        )
        Keyboard(
            characters = DIGITS.substring(0, 10),
            charactersInRow = 5,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            onClick = { onDigitClick(it[0]) }
        )
        Keyboard(
            characters = DIGITS.substring(10, DIGITS.length) + '.',
            charactersInRow = 4,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            onClick = { onDigitClick(it[0]) }
        )
        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = onOkClicked,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorResource(id = R.color.btnBoundsColor)
            )
        ) {
            Text(
                text = "OK",
                fontSize = 30.sp,
            )
        }
    }
}

@Composable
fun KeyboardFragmentDesign(
    viewModel: KeyboardFragmentViewModel,
    onOkClicked: () -> Unit = {}
) {
    val text by viewModel.textFieldState.observeAsState("")
    Scaffold { paddings ->
        KeyboardScreen(
            text = text,
            onBackspaceClicked = { viewModel.backspace() },
            onDigitClick = { c ->
                if (c in DIGITS)
                    viewModel.addDigit(c)
                else if (c == '.')
                    viewModel.addPoint()
            },
            onOkClicked = onOkClicked,
            paddings = paddings
        )
    }
}