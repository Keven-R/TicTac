package com.cnc.tictac.ui.screens

// COMPONENT imports

// RESOURCES import
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.ui.components.DisplayButton
import com.cnc.tictac.ui.components.LogoText
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.string as copy

@Composable
fun HomeScreen(navController: NavHostController,viewModel: TicTacViewModel) {
    // Determine UI layout.
    val deviceInfo = getDeviceInfo()

    // Use different layout for mobile landscape only
    when (deviceInfo.screenHeightType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayWideHomeScreen(navController, viewModel)
            // TEMP for testing
//            GameScreen(navController, viewModel)
        }
        else -> {
            DisplayNarrowHomeScreen(navController, viewModel)
//             TEMP for testing
//            GameScreen(navController, viewModel)
        }
    }
}

/* COMPOSABLE
 * DisplayNarrowHomeScreen
 *
 * UI for home screen for the following devices and orientation:
 *      Mobile portrait, Tablets and greater
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayNarrowHomeScreen(navController: NavHostController,viewModel: TicTacViewModel) {
    // UI: Screen container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 64.dp,
                    bottom = 120.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // UI: App logo
            LogoText(modifier = Modifier.fillMaxWidth())

            // UI: Home screen actions (start game + view profile)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                // UI: Solo or multi game mode action
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DisplayButton(stringResource(id = copy.button_play_solo),viewModel,TicTacEvent.NewSinglePlayerGame,navController,Destination.GameSettingsScreen)
                    DisplayButton(stringResource(id = copy.button_play_multi),viewModel,TicTacEvent.NewMultiPlayerGame,navController,Destination.MultiplayerSettingsScreen)
                }
                DisplayButton(stringResource(id = copy.button_profile),viewModel,TicTacEvent.ProfileMenuSelect,navController,Destination.ProfileScreen)
            }
        }
    }
}

/* COMPOSABLE
 * DisplayExpandedHomeScreen
 *
 * UI for home screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayWideHomeScreen(navController: NavHostController,viewModel: TicTacViewModel) {
    // UI: Screen container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    vertical = 64.dp,
                    horizontal = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // UI: Home screen actions (start game + view profile)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .align(Alignment.Bottom),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // UI: App logo
                LogoText()

                // UI: Solo or multi game mode action
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DisplayButton(stringResource(id = copy.button_play_solo),viewModel,TicTacEvent.NewSinglePlayerGame,navController,Destination.GameSettingsScreen)
                    DisplayButton(stringResource(id = copy.button_play_multi),viewModel,TicTacEvent.NewMultiPlayerGame,navController,Destination.MultiplayerSettingsScreen)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.End) {
                DisplayButton(stringResource(id = copy.button_profile),viewModel, TicTacEvent.ProfileMenuSelect,navController,Destination.ProfileScreen)
            }
        }
    }
}