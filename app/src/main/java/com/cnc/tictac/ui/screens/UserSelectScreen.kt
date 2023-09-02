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
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.PrimaryButton
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun UserSelectScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenWidthType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayDefaultUserSelectScreen(navController)
        }
        is DeviceInfo.DeviceType.Expanded -> {
            DisplayDefaultUserSelectScreen(navController)
        }
        else -> {
            DisplayMediumUserSelectScreen(navController)
        }
    }
}

/* COMPOSABLE
 * DisplayDefaultUserSelectScreen
 *
 * UI for user select screen for the following devices and orientation:
 *      COMPACT (Mobile portrait)
 *      EXPANDED (Tablet landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultUserSelectScreen(navController: NavHostController) {
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
                // TODO: destination + label should change depending on nav controller
                BackButton(
                    stringResource(id = R.string.page_title_switch_user),
                    Destination.ProfileScreen,
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
                // ELEMENTS: Show all available users
                // TODO: MAKE AVATAR CARDS
            }

            // CONTAINER: Main page action, fixed to bottom of screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // BUTTON: Select user to switch to or player to use for game
                // TODO: label + onclick should be either "switch" or select"
                PrimaryButton(
                    stringResource(id = R.string.page_title_select_player),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    println("start game")
                }
            }
        }
    }
}

/* COMPOSABLE
 * DisplayMediumUserSelectScreen
 *
 * UI for user select screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayMediumUserSelectScreen(navController: NavHostController) {
}