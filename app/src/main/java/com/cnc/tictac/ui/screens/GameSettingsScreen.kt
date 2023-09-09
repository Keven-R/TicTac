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
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.string as copy

@Composable
fun GameSettingsScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenHeightType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayShortSettingsScreen(navController)
        }
        else -> {
            DisplayDefaultSettingsScreen(navController)
        }
    }
}

/* COMPOSABLE
 * DisplayDefaultSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      COMPACT (Mobile portrait)
 *      EXPANDED (Tablet landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultSettingsScreen(navController: NavHostController) {
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
                BackButton(
                    stringResource(id = copy.page_title_settings),
                    Destination.HomeScreen,
                    navController,
                )
            }

            // CONTAINER: All game settings found here
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(space = 32.dp),
            ) {
                // ELEMENTS: All setting items needed to start game
                Radio(
                    // TODO: change to "<player 1 name>'s marker"
                    title = "your marker",
                    labels = arrayOf("x", "o"),
                    selectedIndex = 0,
                )

                Radio(
                    title = "who goes first?",
                    // TODO: change labels to "<p1 name>", "<p2 name>"
                    labels = arrayOf("jasmine", "guest"),
                    selectedIndex = 0,
                )

                Radio(
                    title = "board size",
                    labels = arrayOf("3x3", "4x4", "5x5"),
                    selectedIndex = 0,
                )

                // TODO: Remove hard coded args
                Radio(
                    title = "win condition (in a row)",
                    labels = arrayOf("3", "4", "5"),
                    selectedIndex = 0,
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TODO: ADD ACTION HERE TO START GAME
                    println("start game")
                }
            }
        }
    }
}

/* COMPOSABLE
 * DisplayShortSettingsScreen
 *
 * UI for game settings screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayShortSettingsScreen(navController: NavHostController) {
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
                .padding(top = 32.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                // ELEMENT: Back button showing current page title "game settings")
                BackButton(
                    stringResource(id = copy.page_title_settings),
                    Destination.HomeScreen,
                    navController,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )

//                // ELEMENT: Button to start game
//                PrimaryButton(
//                    stringResource(id = copy.settings_action_start),
//                    modifier = Modifier.weight(1f).fillMaxWidth()
//                )
            }

            // CONTAINER: All game settings found here
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space = 32.dp),
            ) {
                // ELEMENTS: All setting items needed to start game
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Radio(
                        // TODO: change to "<player 1 name>'s marker"
                        title = "your marker",
                        labels = arrayOf("x", "o"),
                        selectedIndex = 0,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    Radio(
                        title = "board size",
                        labels = arrayOf("3x3", "4x4", "5x5"),
                        selectedIndex = 0,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Radio(
                        title = "who goes first?",
                        // TODO: change labels to "<p1 name>", "<p2 name>"
                        labels = arrayOf("jasmine", "guest"),
                        selectedIndex = 0,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )

                    Radio(
                        title = "win condition (in a row)",
                        labels = arrayOf("3", "4", "5"),
                        selectedIndex = 0,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }

                // ELEMENT: Button to start game
                PrimaryButton(
                    stringResource(id = copy.settings_action_start),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TODO: ADD ACTION HERE TO START GAME
                    println("start game")
                }
            }
        }
    }
}