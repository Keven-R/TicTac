package com.cnc.tictac.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cnc.tictac.viewmodel.TicTacViewModel

// COMPONENT imports
import com.cnc.tictac.ui.components.PrimaryButton
import com.cnc.tictac.ui.components.ProfileCard

// RESOURCES import
import com.cnc.tictac.R.string as content

@Preview
@Composable
// Added default TicTacViewModel() arg to enable preview
fun MenuScreen(viewModel: TicTacViewModel = TicTacViewModel()) {
    Column() {

        // DESTINATION: Profile page
        // TODO: Remove hardcoded user name, use content from User class instead.
        ProfileCard(user = "Player 1")

        // DESTINATION: Solo game settings
        PrimaryButton(stringResource(id = content.button_gamemode_solo)) {
            println("Game mode: vs AI")
        }

        // DESTINATION: Multiplayer game settings
        PrimaryButton(stringResource(id = content.button_gamemode_multi)) {
            println("Game mode: vs human")
        }
    }
}