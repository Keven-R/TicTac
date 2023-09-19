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
import com.cnc.tictac.ui.components.PrimaryButton
import com.cnc.tictac.ui.components.Radio
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.string as copy

@Composable
fun GameSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Update Radio choices in "who goes first"
    viewModel.startOptions = arrayOf(viewModel.player1Name, viewModel.player2Name)

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenHeightType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayShortSettingsScreen(navController, viewModel)
        }
        else -> {
            DisplayDefaultSettingsScreen(navController, viewModel)
        }
    }
}

/* COMPOSABLE
 * DisplayDefaultSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      COMPACT (Mobile portrait)
 *      EXPANDED (Tablet and bigger)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
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
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CONTAINER: Top nav
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // ELEMENT: Back button showing current page title "game settings")
                BackButton(stringResource(id = copy.page_title_settings),navController,)
            }

            // CONTAINER: All game settings found here
            Column(
                modifier = Modifier
                    .fillMaxHeight().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(space = 32.dp),
            ) {
                // ELEMENTS: All setting items needed to start game
                Radio(
                    title = viewModel.player1Name,
                    viewModel = viewModel,
                    onClick = viewModel.updateP1Marker,
                    labels = viewModel.markerOptions,
                    selectedIndex = viewModel.player1Marker,
                )

                Radio(
                    title = "who goes first?",
                    viewModel = viewModel,
                    onClick = viewModel.updateWhoGoesFirst,
                    labels = viewModel.startOptions,
                    selectedIndex = viewModel.startingSelection,
                )

                Radio(
                    title = "board size",
                    viewModel = viewModel,
                    onClick = viewModel.updateBoardSize,
                    labels = viewModel.boardOptions,
                    selectedIndex = viewModel.boardSelection,
                )

                Radio(
                    title = "win condition (in a row)",
                    viewModel = viewModel,
                    onClick = viewModel.updateWinCondition,
                    labels = viewModel.winOptions,
                    selectedIndex = viewModel.winConditionSelection,
                    isDisabled = viewModel.winSelectable
                )
            }

            // CONTAINER: Main page action, fixed to bottom of screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // ELEMENT: Button to start game
                PrimaryButton(
                    stringResource(id = copy.settings_action_start),
                    navController = navController,
                    destination = Destination.GameScreen,
                    viewModel = viewModel,
                    event = TicTacEvent.StartGame,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/* COMPOSABLE
 * DisplayShortSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayShortSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
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
                // ELEMENT: Back button showing current page title "game settings")
                BackButton(stringResource(id = copy.page_title_settings), navController) }

            // CONTAINER: All game settings found here
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                // ELEMENTS: All setting items needed to start game
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Radio(
                        title = viewModel.player1Name,
                        viewModel = viewModel,
                        onClick = viewModel.updateP1Marker,
                        labels = viewModel.markerOptions,
                        selectedIndex = viewModel.player1Marker,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Radio(
                        title = "board size",
                        viewModel = viewModel,
                        onClick = viewModel.updateBoardSize,
                        labels = viewModel.boardOptions,
                        selectedIndex = viewModel.boardSelection,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Radio(
                        title = "who goes first?",
                        viewModel = viewModel,
                        onClick = viewModel.updateWhoGoesFirst,
                        labels = viewModel.startOptions,
                        selectedIndex = viewModel.startingSelection,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    Radio(
                        title = "win condition (in a row)",
                        viewModel = viewModel,
                        onClick = viewModel.updateWinCondition,
                        labels = viewModel.winOptions,
                        selectedIndex = viewModel.winConditionSelection,
                        isDisabled = viewModel.winSelectable,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }

            // CONTAINER: Primary action
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Button to start game
                PrimaryButton(
                    stringResource(id = copy.settings_action_start),
                    navController = navController,
                    destination = Destination.GameScreen,
                    viewModel = viewModel,
                    event = TicTacEvent.StartGame,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}