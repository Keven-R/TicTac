package com.cnc.tictac.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.GameMenuButtonGroup
import com.cnc.tictac.ui.components.GamePlayerCard
import com.cnc.tictac.ui.components.PLAYERWINSTATUS
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
                .padding(top = 24.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ELEMENT: player 1
            // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
            GamePlayerCard(
                modifier = Modifier.fillMaxWidth(),
                isRowLayout = true,

                playerName = "jasmine",                         // TODO: ADD player name
                playerAvatarResourceId = R.drawable.avatar_7,   // TODO: ADD player resource id
                playerMarker = "x",                             // TODO: ADD player marker
                isGameEnded = false,                            // TODO: Game status

                playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: REQUIRED IF game ended

                isPlayerTurn = true,                            // TODO: OPTIONAL, required if game ongoing
                secondsLeft = 8,                                // TODO: OPTPONAL, required if game ongoing
            )

            // CONTAINER: game board
            // TODO: change to lazy grid
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            }

            // ELEMENT: player 2
            // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
            GamePlayerCard(
                modifier = Modifier.fillMaxWidth(),
                isRowLayout = true,

                playerName = "guest",                           // TODO: ADD player name
                playerAvatarResourceId = R.drawable.avatar_2,   // TODO: ADD player resource id
                playerMarker = "0",                             // TODO: ADD player marker
                isGameEnded = false,                            // TODO: Game status

                playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: OPTIONAL, required if game ended

                isPlayerTurn = false,                           // TODO: OPTIONAL, required if game ongoing
                secondsLeft = 10,                               // TODO: OPTPONAL, required if game ongoing
            )

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