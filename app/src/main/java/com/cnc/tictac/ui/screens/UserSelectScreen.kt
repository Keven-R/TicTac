package com.cnc.tictac.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.NewUserCardButton
import com.cnc.tictac.ui.components.PrimaryButton
import com.cnc.tictac.ui.components.UserCell
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun UserSelectScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()

    // Use same UI layout for COMPACT and EXPANDED
    when (deviceInfo.screenHeightType) {
        is DeviceInfo.DeviceType.Compact -> {
            DisplayCompactUserSelectScreen(navController, viewModel)
        }
        else -> {
            DisplayDefaultUserSelectScreen(navController, viewModel)
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
fun DisplayDefaultUserSelectScreen(navController: NavHostController, viewModel: TicTacViewModel) {
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
                // ELEMENT: Back button showing current page title
                // either "page_title_select_player" or "page_title_switch_user"
                BackButton(
                    stringResource(id =  when(viewModel.playerSwitchUI){
                        true -> R.string.page_title_switch_user
                        false -> R.string.page_title_select_player
                    }),
                    navController,
                )
            }

            // CONTAINER: Avatar select found here {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 24.dp),
                columns = GridCells.Adaptive(minSize = 140.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                userScrollEnabled = true,
                content = {
                    items(viewModel.users.size) { i ->
                        when (i) {
                            0 -> {
                                // ELEMENT: NEW USER button
                                NewUserCardButton(
                                    modifier = Modifier,
                                    viewModel = viewModel,
                                    navController = navController,
                                    label = stringResource(id = R.string.button_new_user_label),
                                    icon = stringResource(id = R.string.button_new_user_icon),
                                    boxModifier = Modifier.fillMaxWidth(),
                                )
                            } else -> {
                                // ELEMENT: All users
                                UserCell(
                                    modifier = Modifier.fillMaxWidth(),
                                    playerName = viewModel.users[i-1]!!.playerName,
                                    avatarResourceId = viewModel.users[i-1]!!.playerAvatar,
                                    isSelected = i-1 == viewModel.userSelectIndex,
                                    viewModel = viewModel,
                                    position = i-1
                                )
                            }
                        }
                    }
                }
            )

            // CONTAINER: Main page action, fixed to bottom of screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // BUTTON: Primary action
                PrimaryButton(
                    stringResource(id = when(viewModel.playerSwitchUI){
                        true -> R.string.switch_user_action
                        false -> R.string.select_player_action
                    }),
                    navController = navController,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}

/* COMPOSABLE
 * DisplayCompactUserSelectScreen
 *
 * UI for user select screen for the following devices and orientation:
 *      MEDIUM (Mobile landscape, tablet portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayCompactUserSelectScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title
                // either "page_title_select_player" or "page_title_switch_user"
                BackButton(
                    stringResource(id = when(viewModel.playerSwitchUI){
                        true -> R.string.page_title_switch_user
                        false -> R.string.page_title_select_player
                    }),
                    navController,
                )
            }

            // CONTAINER: Avatar select found here {
            LazyHorizontalGrid(
                modifier = Modifier
                    .weight(1f).fillMaxHeight()
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                rows = GridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
                userScrollEnabled = true,
                content = {
                    items(viewModel.users.size) { i ->
                        when (i) {
                            0 -> {
                                // ELEMENT: NEW USER button
                                NewUserCardButton(
                                    modifier = Modifier.fillMaxHeight(),
                                    viewModel = viewModel,
                                    navController = navController,
                                    label = stringResource(id = R.string.button_new_user_label),
                                    icon = stringResource(id = R.string.button_new_user_icon),
                                    boxModifier = Modifier.fillMaxHeight().weight(1f),
                                )
                            } else -> {
                            // ELEMENT: All users
                                UserCell(
                                    modifier = Modifier.fillMaxHeight(),
                                    avatarModifier = Modifier.fillMaxHeight().weight(1f).aspectRatio(1f),
                                    playerName = viewModel.users[i-1].playerName,
                                    avatarResourceId = viewModel.users[i-1].playerAvatar,
                                    isSelected = i-1 == viewModel.userSelectIndex,
                                    viewModel = viewModel,
                                    position = i-1
                                )
                            }
                        }
                    }
                }
            )

            // CONTAINER: Primary action
            Row(modifier = Modifier.fillMaxWidth()) {
                // BUTTON: Primary action
                PrimaryButton(
                    stringResource(id = when(viewModel.playerSwitchUI){
                        true -> R.string.switch_user_action
                        false -> R.string.select_player_action
                    }),
                    navController = navController,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}