package com.cnc.tictac.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cnc.tictac.viewmodel.TicTacViewModel

@Preview
@Composable
// Added default TicTacViewModel() arg to enable preview
fun MenuScreen(viewModel: TicTacViewModel = TicTacViewModel()) {
    Button()
}