package com.cnc.tictac.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cnc.tictac.ui.components.SimpleButton
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun MenuScreen(viewModel: TicTacViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Tic Tac", fontSize = 80.sp)
            Text(text = "Enter Name", fontSize = 25.sp)
            TextField(
                value = viewModel.tempEditText,
                onValueChange = {viewModel.tempEditText = it},
                modifier = Modifier
                    .padding(8.dp)
            )
            SimpleButton(label = "Click me", onClickEvent = {viewModel.onEvent(TicTacEvent.tempClick)})
            Text(text = viewModel.tempText, fontSize = 25.sp)
        }
    }
}