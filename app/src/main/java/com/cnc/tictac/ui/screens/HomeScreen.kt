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
import com.cnc.tictac.ui.components.logoText
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.R.string as copy

@Composable
fun HomeScreen(navController: NavHostController) {
    // Determine UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenHeightType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayWideHomeScreen(navController)
        }
        is DeviceInfo.DeviceType.Medium -> {
            DisplayNarrowHomeScreen(navController)
        }
        else -> {
            DisplayWideHomeScreen(navController)
        }
    }
}

/* COMPOSABLE
 * DisplayNarrowHomeScreen
 *
 * UI for home screen for the following devices and orientation:
 *      COMPACT (Mobile portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayNarrowHomeScreen(navController: NavHostController) {
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
            logoText(modifier = Modifier.fillMaxWidth())

            // UI: Home screen actions (start game + view profile)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                // UI: Solo or multi game mode action
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // TODO: Add correct destination to on click event
                    DisplayButton(stringResource(id = copy.button_play_solo),navController, Destination.GameSettingsScreen)
                    // TODO: Add correct destination to on click event
                    DisplayButton(stringResource(id = copy.button_play_multi), navController, Destination.GameSettingsScreen)
                }

                // UI: View profile action
                // TODO: Add correct destination to on click event
                DisplayButton(stringResource(id = copy.button_profile), navController, Destination.ProfileScreen)
            }
        }
    }
}

/* COMPOSABLE
 * DisplayExpandedHomeScreen
 *
 * UI for home screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *      EXPANDED (Tablet landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayWideHomeScreen(navController: NavHostController) {
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
                logoText()

                // UI: Solo or multi game mode action
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // TODO: Add correct destination to on click event
                    DisplayButton(stringResource(id = copy.button_play_solo),navController, Destination.GameSettingsScreen)
                    // TODO: Add correct destination to on click event
                    DisplayButton(stringResource(id = copy.button_play_multi), navController, Destination.GameSettingsScreen)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.End) {
                // UI: View profile action
                // TODO: Add correct destination to on click event
                DisplayButton(stringResource(
                    id = copy.button_profile),
                    navController,
                    Destination.ProfileScreen,
                )
            }
        }
    }
}