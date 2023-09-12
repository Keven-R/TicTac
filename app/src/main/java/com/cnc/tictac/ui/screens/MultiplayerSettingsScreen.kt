package com.cnc.tictac.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.PlayerSelectCard
import com.cnc.tictac.ui.components.PrimaryButton
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.string as copy

@Composable
fun MultiplayerSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenWidthType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayCompactMultiplayerSettingsScreen(navController,viewModel)
        }
        else -> {
            DisplayDefaultMultiplayerSettingsScreen(navController,viewModel)
        }
    }
}

/* COMPOSABLE
 * DisplayCompactMultiplayerSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      COMPACT (Mobile portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayCompactMultiplayerSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title)
                BackButton(stringResource(id = copy.page_title_multiplayer),Destination.HomeScreen,navController)
            }

            // CONTAINER: for both player cards
            Row(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .weight(1f).fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                // ELEMENT: display current selected player 1
                PlayerSelectCard(
                    navController = navController,
                    destination = Destination.UserSelectScreen,
                    playerName = viewModel.player1,
                    avatarResourceId = viewModel.player1Avatar,
                    isHorizontal = false,
                    layoutModifier = Modifier.fillMaxWidth().weight(1f),
                )

                // ELEMENT: display current selected player 2
                PlayerSelectCard(
                    navController = navController,
                    destination = Destination.UserSelectScreen,
                    playerName = viewModel.player2,
                    isPlayerOne = false,
                    avatarResourceId = viewModel.player2Avatar,
                    isHorizontal = false,
                    layoutModifier = Modifier.fillMaxWidth().weight(1f),
                )
            }

            // CONTAINER: Primary action
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Button to go to game settings screen
                PrimaryButton(
                    stringResource(id = copy.settings_action_next),
                    navController = navController,
                    destination = Destination.GameSettingsScreen,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/* COMPOSABLE
 * DisplayDefaultMultiplayerSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      Mobile landscape
 *      Tablet
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultMultiplayerSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title)
                BackButton(stringResource(id = copy.page_title_multiplayer),Destination.HomeScreen,navController)
            }

            // CONTAINER: for both player cards
            Row(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .weight(1f).fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(space = 32.dp)
            ){
                // ELEMENT: display current selected player 1
                PlayerSelectCard(
                    navController = navController,
                    destination = Destination.UserSelectScreen,
                    playerName = viewModel.player1,
                    avatarResourceId = viewModel.player1Avatar,
                    layoutModifier = Modifier.weight(1f).fillMaxWidth(),
                )

                // ELEMENT: display current selected player 2
                PlayerSelectCard(
                    navController = navController,
                    destination = Destination.UserSelectScreen,
                    playerName = viewModel.player2,
                    isPlayerOne = false,
                    avatarResourceId = viewModel.player2Avatar,
                    layoutModifier = Modifier.weight(1f).fillMaxWidth(),
                )
            }

            // CONTAINER: Primary action
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Button to go to game settings screen
                PrimaryButton(
                    stringResource(id = copy.settings_action_next),
                    navController = navController,
                    destination = Destination.GameSettingsScreen,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}