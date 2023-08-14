package com.cnc.tictac.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleButton(
    label:String,
    onClickEvent: () -> Unit
) {
    Button(
        onClick = onClickEvent,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = label, fontSize = 22.sp, textAlign = TextAlign.Center)
    }
}