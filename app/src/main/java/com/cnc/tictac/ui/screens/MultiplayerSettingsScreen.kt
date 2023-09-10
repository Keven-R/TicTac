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
import com.cnc.tictac.R.drawable as images
import com.cnc.tictac.R.string as copy

@Composable
fun MultiplayerSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenWidthType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayCompactMultiplayerSettingsScreen(navController)
        }
        else -> {
            DisplayDefaultMultiplayerSettingsScreen(navController)
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
fun DisplayCompactMultiplayerSettingsScreen(navController: NavHostController) {
}

/* COMPOSABLE
 * DisplayShortSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      Mobile landscape
 *      Tablet
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultMultiplayerSettingsScreen(navController: NavHostController) {
    // CONTAINER: Set bg colour
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title)
                BackButton(
                    stringResource(id = copy.page_title_multiplayer),
                    Destination.HomeScreen,
                    navController
                )
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
                // TODO: UPDATE THIS with actual player information
                PlayerSelectCard(
                    playerName = "Jasmine",
                    avatarResourceId = images.avatar_1
                )

                // ELEMENT: display current selected player 2
                // TODO: UPDATE THIS with actual player information
                PlayerSelectCard(
                    playerName = "Guest",
                    isPlayerOne = false,
                    avatarResourceId = images.avatar_2
                )

                // CONTAINER: Primary action
                Row(modifier = Modifier.fillMaxWidth()) {
                    // ELEMENT: Button to go to game settings screen
                    PrimaryButton(
                        stringResource(id = copy.settings_action_next),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // TODO: ADD ACTION HERE TO START GAME
                        println("go to game settings")
                    }
                }
            }
        }
    }
}