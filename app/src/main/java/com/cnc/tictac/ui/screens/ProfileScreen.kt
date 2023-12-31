package com.cnc.tictac.ui.screens

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.ui.components.Avatar
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.BodyMedium
import com.cnc.tictac.ui.components.MarkerGraphics
import com.cnc.tictac.ui.components.SecondaryButton
import com.cnc.tictac.ui.components.TitleMedium
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.viewmodel.UIPLAYERSELECT
import com.cnc.tictac.R.string as copy

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        if (deviceInfo.screenHeight < 600.dp) {
            DisplayShortProfileScreen(navController, viewModel)
        } else {
            DisplayDefaultProfileScreen(navController, viewModel)
        }
    } else {
        DisplayDefaultProfileScreen(navController, viewModel)
    }
}

/* COMPOSABLE
 * DisplayDefaultProfileScreen
 *
 * UI for profile screen for the following devices and orientation:
 *      MEDIUM HEIGHT (Mobile portrait)
 *      EXPANDED HEIGHT (Tablet and bigger)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayDefaultProfileScreen(navController: NavHostController, viewModel: TicTacViewModel) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // CONTAINER: Top nav
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // ELEMENT: Back button showing current page title "profile")
                BackButton(stringResource(id = copy.page_title_profile),navController)
            }

            // CONTAINER: User info and stats
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ELEMENT: User name + avatar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Avatar(
                            avatarResourceId = viewModel.player1Avatar,
                            imageModifier = Modifier.size(160.dp)
                        )
                    }

                    TitleMedium(viewModel.player1Name, modifier = Modifier.fillMaxWidth())
                }

                // CONTAINER: User's stats
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ELEMENT: win ratio block
                    MarkerGraphics(content = viewModel.player1StatMarker, rowModifier = Modifier.fillMaxWidth())

                    BodyMedium(content = viewModel.player1WinString, modifier = Modifier.fillMaxWidth().padding(top = 24.dp))
                    BodyMedium(content = viewModel.player1DrawString, modifier = Modifier.fillMaxWidth())
                    BodyMedium(content = viewModel.player1LossesString, modifier = Modifier.fillMaxWidth())
                    BodyMedium(content = viewModel.player1TotalGamesString, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
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
                if(viewModel.player1.playerID != 0){
                    SecondaryButton(
                        stringResource(id = copy.profile_action_left),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER1
                        viewModel.findEditTextValue()
                        viewModel.selectedAvatar = viewModel.findAvatar()
                        navController.navigate(Destination.UserDetailScreen.route)
                    }
                }

                // ELEMENT: Button to navigate to "switch user" screen
                SecondaryButton(
                    stringResource(id = copy.profile_action_right),
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER1
                    viewModel.userSelectIndex = viewModel.findUser()
                    navController.navigate(Destination.UserSelectScreen.route)
                }
            }
        }
    }
}

/* COMPOSABLE
 * DisplayShortProfileScreen
 *
 * UI for profile screen for the following devices and orientation:
 *      COMPACT HEIGHT (Mobile landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayShortProfileScreen(navController: NavHostController, viewModel: TicTacViewModel) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title "profile")
                BackButton(stringResource(id = copy.page_title_profile),navController)
            }

            // CONTAINER: All profile scores
            Row(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .weight(1f).fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ELEMENT: User name + avatar
                Column(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Avatar(
                        avatarResourceId = viewModel.player1Avatar,
                        imageModifier = Modifier.size(160.dp)
                    )

                    TitleMedium(viewModel.player1Name)
                }

                // CONTAINER: User's stats
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // ELEMENT: win ratio block
                    MarkerGraphics(content = viewModel.player1StatMarker, alignCenter = false)

                    BodyMedium(content = viewModel.player1WinString, modifier = Modifier.padding(top = 16.dp))
                    BodyMedium(content = viewModel.player1DrawString)
                    BodyMedium(content = viewModel.player1LossesString)
                    BodyMedium(content = viewModel.player1TotalGamesString, modifier = Modifier.padding(top = 8.dp))
                }
            }

            // CONTAINER: Page actions, fixed to bottom of screen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                // ELEMENT: Button to navigate to "edit profile" screen
                if(viewModel.player1.playerID != 0){
                    SecondaryButton(
                        stringResource(id = copy.profile_action_left),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER1
                        viewModel.findEditTextValue()
                        viewModel.selectedAvatar = viewModel.findAvatar()
                        navController.navigate(Destination.UserDetailScreen.route)
                    }
                }

                // ELEMENT: Button to navigate to "switch user" screen
                SecondaryButton(
                    stringResource(id = copy.profile_action_right),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER1
                    viewModel.userSelectIndex = viewModel.findUser()
                    navController.navigate(Destination.UserSelectScreen.route)
                }
            }
        }
    }
}