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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.SecondaryButton
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenWidthType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayDefaultProfileScreen(navController)
        }
        is DeviceInfo.DeviceType.Expanded -> {
            DisplayDefaultProfileScreen(navController)
        }
        else -> {
            DisplayMediumProfileScreen(navController)
        }
    }
}

/* COMPOSABLE
 * DisplayDefaultProfileScreen
 *
 * UI for home screen for the following devices and orientation:
 *      COMPACT (Mobile portrait)
 *      EXPANDED (Tablet landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultProfileScreen(navController: NavHostController) {
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
                // ELEMENT: Back button showing current page title "profile")
                BackButton(
                    stringResource(id = R.string.page_title_profile),
                    Destination.HomeScreen,
                    navController,
                )
            }

            // TODO: content fill container
            // CONTAINER: User info and stats
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.CenterVertically)
            ) {

            }

            // CONTAINER: Page actions, fixed to bottom of screen
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                // ELEMENT: Button to navigate to "edit profile" screen
                SecondaryButton(
                    stringResource(id = R.string.profile_action_left),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    // TODO: ADD ACTION HERE TO GO TO EDIT PROFILE
                    println("edit profile")
                }

                // ELEMENT: Button to navigate to "switch user" screen
                SecondaryButton(
                    stringResource(id = R.string.profile_action_right),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // TODO: ADD ACTION HERE TO GO TO SWITCH USER
                    println("switch user")
                }
            }
        }
    }
}



/* COMPOSABLE
 * DisplayMediumProfileScreen
 *
 * UI for home screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayMediumProfileScreen(navController: NavHostController) {

}