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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.ui.components.GameMenuButtonGroup
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun GameScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenWidthType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayPortraitGameScreen(navController, viewModel)
        }
        is DeviceInfo.DeviceType.Expanded -> {
            DisplayPortraitGameScreen(navController, viewModel)
        }
        else -> {
            DisplayPortraitGameScreen(navController, viewModel)
        }
    }
}

/* COMPOSABLE
 * DisplayPortraitGameScreen
 *
 * UI for game screen for all portrait orientation devices:
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayPortraitGameScreen(navController: NavHostController, viewModel: TicTacViewModel) {
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
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CONTAINER: top user
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

            }

            // CONTAINER: game board
            // TODO: change to lazy grid
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            }

            // CONTAINER: bottom user
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
            }

            // CONTAINER: all menu controls
            // TODO: remove "enableUndo" arg if undo is available to use
            GameMenuButtonGroup(enableUndo = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

/* COMPOSABLE
 * DisplayMediumGameScreen
 *
 * UI for game screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayMediumGameScreen(navController: NavHostController, viewModel: TicTacViewModel) {
}