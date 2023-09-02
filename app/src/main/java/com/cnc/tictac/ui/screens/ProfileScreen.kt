package com.cnc.tictac.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.SecondaryButton
import com.cnc.tictac.ui.components.bodyMedium
import com.cnc.tictac.ui.components.titleMedium
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.drawable as images
import com.cnc.tictac.R.string as copy

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
 * UI for profile screen for the following devices and orientation:
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
                    stringResource(id = copy.page_title_profile),
                    Destination.HomeScreen,
                    navController,
                )
            }

            // CONTAINER: User info and stats
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(
                    space = 32.dp,
                    alignment = Alignment.CenterVertically
                ),
            ) {

                // ELEMENT: User name + avatar
                // TODO: Change painter to user selected avatar.
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = images.avatar_1),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(160.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }

                    // TODO: Replace string with user's name.
                    titleMedium("jasmine", modifier = Modifier.fillMaxWidth())
                }

                // CONTAINER: User's stats
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // TODO: replace with actual stats
                    bodyMedium(content = "wins: 8 (33%)", modifier = Modifier.fillMaxWidth())
                    bodyMedium(content = "draws: 13 (54%)", modifier = Modifier.fillMaxWidth())
                    bodyMedium(content = "losses: 3 (13%)", modifier = Modifier.fillMaxWidth())
                    bodyMedium(content = "total games 24", modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                }
            }

            // CONTAINER: Page actions, fixed to bottom of screen
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                // ELEMENT: Button to navigate to "edit profile" screen
                SecondaryButton(
                    stringResource(id = copy.profile_action_left),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // TODO: ADD ACTION HERE TO GO TO EDIT PROFILE
                    println("edit profile")
                }

                // ELEMENT: Button to navigate to "switch user" screen
                SecondaryButton(
                    stringResource(id = copy.profile_action_right),
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
 * UI for profile screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayMediumProfileScreen(navController: NavHostController) {

}