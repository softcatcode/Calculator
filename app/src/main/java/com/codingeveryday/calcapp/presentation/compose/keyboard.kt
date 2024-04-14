package com.codingeveryday.calcapp.presentation.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingeveryday.calcapp.R
import com.codingeveryday.calcapp.domain.entities.Expression.Companion.DIGITS
import com.codingeveryday.calcapp.presentation.KeyboardFragmentViewModel
import kotlin.math.min

@Composable
fun ButtonRow(
    modifier: Modifier,
    labelList: List<String>,
    onClick: (String) -> Unit
) {
    Row(modifier) {
        labelList.forEach {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(2.dp),
                onClick = { onClick(it) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = colorResource(id = R.color.purple_200),
                ),
                border = BorderStroke(2.dp, colorResource(id = R.color.purple_500))
            ) {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AllDigitsKeyboard(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Column(modifier) {
        var i = 0
        val n = 6
        while (i < DIGITS.length) {
            val labels = DIGITS.substring(i, min(i + n, DIGITS.length))
                .toCharArray().map { "$it" }
            ButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .weight(1f),
                labelList = labels,
                onClick = onClick
            )
            i += n
        }
    }
}

@Composable
fun KeyboardFragmentDesign(
    viewModel: KeyboardFragmentViewModel,
    onOkClicked: () -> Unit
) {
    val text = viewModel.textFieldState.observeAsState("")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(Color.White),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors().copy(containerColor = Color.White),
            border = BorderStroke(2.dp, colorResource(id = R.color.purple_500))
        ) {
            Text(
                text = text.value,
                color = Color.Black,
                fontSize = 30.sp,
            )
        }
        AllDigitsKeyboard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(Color.White),
            onClick = { viewModel.addDigit(it[0]) }
        )
        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = onOkClicked,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorResource(id = R.color.teal_200)
            )
        ) {
            Text(
                text = "OK",
                fontSize = 30.sp,
            )
        }
    }
}