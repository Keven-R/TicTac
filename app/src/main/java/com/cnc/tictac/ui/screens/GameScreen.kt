package com.cnc.tictac.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.ui.components.GameBoard
import com.cnc.tictac.ui.components.GameMenuButtonGroup
import com.cnc.tictac.ui.components.GamePlayerCard
import com.cnc.tictac.ui.system.DeviceInfo
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun GameScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        when (deviceInfo.screenHeightType) {
            is DeviceInfo.DeviceType.Compact -> {
                DisplayGameScreenLandscapeMobile(navController, viewModel)
            } is DeviceInfo.DeviceType.Medium -> {
                DisplayGameScreenLandscape(navController, viewModel)
            } else -> {
                DisplayGameScreenLandscape(navController, viewModel)
            }
        }
    } else {
        if (deviceInfo.screenWidth / deviceInfo.screenHeight < 0.5) {
            DisplayGameScreenPortraitMobile(navController, viewModel)
        } else if (deviceInfo.screenWidth / deviceInfo.screenHeight < 0.7) {
            DisplayGameScreenPortraitMedium(navController, viewModel)
        } else {
            DisplayGameScreenPortrait(navController, viewModel)
        }
    }
}

/* COMPOSABLE
 * DisplayGameScreenLandscapeMobile
 *
 * UI for game screen for the following devices and orientation:
 *      COMPACT HEIGHT (Mobile landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayGameScreenLandscapeMobile(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        // CONTAINER: All content on screen
        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            // CONTAINER: Left side
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // CONTAINER: Game player cards
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween) {

                    // ELEMENT: player 1
                    GamePlayerCard(
                        modifier = Modifier,
                        isRowLayout = true,

                        playerName = viewModel.player1Name,
                        playerAvatarResourceId = viewModel.player1Avatar,
                        playerMarker = viewModel.getMarkerSymbol(viewModel.player1Marker),
                        isGameEnded = !viewModel.gameActive,

                        playerWinStatus = viewModel.player1WinStatus,

                        isPlayerTurn = viewModel.player1Turn,
                        secondsLeft = viewModel.player1Timer,
                    )

                    // ELEMENT: player 2
                    GamePlayerCard(
                        modifier = Modifier,
                        isRowLayout = true,

                        playerName = viewModel.player2Name,
                        playerAvatarResourceId = viewModel.player2Avatar,
                        playerMarker = viewModel.getMarkerSymbol(viewModel.player2Marker),
                        isGameEnded = !viewModel.gameActive,

                        playerWinStatus = viewModel.player2WinStatus,

                        isPlayerTurn = viewModel.player2Turn,
                        secondsLeft = viewModel.player2Timer,
                    )
                }

                // ELEMENT: all menu controls
                GameMenuButtonGroup(viewModel = viewModel, navController = navController, modifier = Modifier.fillMaxWidth())
            }

            // CONTAINER: right side (has game board)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                GameBoard(
                    modifier = Modifier.fillMaxHeight(),
                    viewModel = viewModel,
                    isContainerNarrow = false
                )
            }
        }
    }
}

/* COMPOSABLE
 * DisplayGameScreenLandscape
 *
 * UI for game screen for the following devices and orientation:
 *      COMPACT HEIGHT (Mobile landscape)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayGameScreenLandscape(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        // CONTAINER: All content on screen
        Column (
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // CONTAINER: Left + right side of main content
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(80.dp)
            ) {
                // CONTAINER: Left side (game player cards)
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // ELEMENT: player 1
                    GamePlayerCard(
                        modifier = Modifier,
                        isRowLayout = true,

                        playerName = viewModel.player1Name,
                        playerAvatarResourceId = viewModel.player1Avatar,
                        playerMarker = viewModel.getMarkerSymbol(viewModel.player1Marker),
                        isGameEnded = !viewModel.gameActive,

                        playerWinStatus = viewModel.player1WinStatus,

                        isPlayerTurn = viewModel.player1Turn,
                        secondsLeft = viewModel.player1Timer,
                    )

                    // ELEMENT: player 2
                    GamePlayerCard(
                        modifier = Modifier,
                        isRowLayout = true,

                        playerName = viewModel.player2Name,
                        playerAvatarResourceId = viewModel.player2Avatar,
                        playerMarker = viewModel.getMarkerSymbol(viewModel.player2Marker),
                        isGameEnded = !viewModel.gameActive,

                        playerWinStatus = viewModel.player2WinStatus,

                        isPlayerTurn = viewModel.player2Turn,
                        secondsLeft = viewModel.player2Timer,
                    )
                }

                // CONTAINER: right side (game board)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    // ELEMENT: game board (occupy all right side)
                    GameBoard(
                        modifier = Modifier.fillMaxHeight(),
                        viewModel = viewModel,
                        isContainerNarrow = false
                    )
                }
            }

            // ELEMENT: all menu controls (fixed bottom)
            GameMenuButtonGroup(viewModel = viewModel, navController = navController, modifier = Modifier.fillMaxWidth())
        }
    }
}


/* COMPOSABLE
 * DisplayGameScreenPortraitMobile
 *
 * UI for game screen for the following devices and orientation:
 *      COMPACT WIDTH (Mobile portrait)
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayGameScreenPortraitMobile(navController: NavHostController, viewModel: TicTacViewModel) {
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
            GamePlayerCard(
                modifier = Modifier.fillMaxWidth(),
                isRowLayout = true,

                playerName = viewModel.player1Name,
                playerAvatarResourceId = viewModel.player1Avatar,
                playerMarker = viewModel.getMarkerSymbol(viewModel.player1Marker),
                isGameEnded = !viewModel.gameActive,

                playerWinStatus = viewModel.player1WinStatus,

                isPlayerTurn = viewModel.player1Turn,
                secondsLeft = viewModel.player1Timer,
            )

            // CONTAINER: game board
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                GameBoard(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel
                )
            }

            // ELEMENT: player 2
            GamePlayerCard(
                modifier = Modifier.fillMaxWidth(),
                isRowLayout = true,

                playerName = viewModel.player2Name,
                playerAvatarResourceId = viewModel.player2Avatar,
                playerMarker = viewModel.getMarkerSymbol(2),
                isGameEnded = !viewModel.gameActive,

                playerWinStatus = viewModel.player2WinStatus,

                isPlayerTurn = viewModel.player2Turn,
                secondsLeft = viewModel.player2Timer,
            )

            // CONTAINER: all menu controls
            GameMenuButtonGroup(viewModel = viewModel, navController = navController, modifier = Modifier.fillMaxWidth())
        }
    }
}



/* COMPOSABLE
 * DisplayGameScreenPortraitMedium
 *
 * UI for game screen for the following devices and orientation:
 *      Portrait MEDIUM+ width
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayGameScreenPortraitMedium(navController: NavHostController, viewModel: TicTacViewModel) {
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

            // CONTAINER: Player Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ELEMENT: player 1
                GamePlayerCard(
                    isRowLayout = true,
                    showTimer = false,

                    playerName = viewModel.player1Name,
                    playerAvatarResourceId = viewModel.player1Avatar,
                    playerMarker = viewModel.getMarkerSymbol(1),
                    isGameEnded = !viewModel.gameActive,

                    playerWinStatus = viewModel.player1WinStatus,

                    isPlayerTurn = viewModel.player1Turn,
                    secondsLeft = viewModel.player1Timer,
                )

                // ELEMENT: player 2
                GamePlayerCard(
                    isRowLayout = true,
                    inverse = true,
                    showTimer = false,

                    playerName = viewModel.player2Name,
                    playerAvatarResourceId = viewModel.player2Avatar,
                    playerMarker = viewModel.getMarkerSymbol(viewModel.player2Marker),
                    isGameEnded = !viewModel.gameActive,

                    playerWinStatus = viewModel.player2WinStatus,

                    isPlayerTurn = viewModel.player2Turn,
                    secondsLeft = viewModel.player2Timer,
                )
            }

            // CONTAINER: game board
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                GameBoard(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel
                )
            }

            // CONTAINER: all menu controls
            GameMenuButtonGroup(viewModel = viewModel, navController = navController, modifier = Modifier.fillMaxWidth())
        }
    }
}

/* COMPOSABLE
 * DisplayGameScreenPortrait
 *
 * UI for game screen for the following devices and orientation:
 *      Portrait MEDIUM+ width
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
@Composable
fun DisplayGameScreenPortrait(navController: NavHostController, viewModel: TicTacViewModel) {
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

            // CONTAINER: Player Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ELEMENT: player 1
                GamePlayerCard(
                    isRowLayout = true,

                    playerName = viewModel.player1Name,
                    playerAvatarResourceId = viewModel.player1Avatar,
                    playerMarker = viewModel.getMarkerSymbol(viewModel.player1Marker),
                    isGameEnded = !viewModel.gameActive,

                    playerWinStatus = viewModel.player1WinStatus,

                    isPlayerTurn = viewModel.player1Turn,
                    secondsLeft = viewModel.player1Timer,
                )

                // ELEMENT: player 2
                GamePlayerCard(
                    isRowLayout = true,
                    inverse = true,

                    playerName = viewModel.player2Name,
                    playerAvatarResourceId = viewModel.player2Avatar,
                    playerMarker = viewModel.getMarkerSymbol(viewModel.player2Marker),
                    isGameEnded = !viewModel.gameActive,

                    playerWinStatus = viewModel.player2WinStatus,

                    isPlayerTurn = viewModel.player2Turn,
                    secondsLeft = viewModel.player2Timer,
                )
            }

            // CONTAINER: game board
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                GameBoard(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel
                )
            }

            // CONTAINER: all menu controls
            GameMenuButtonGroup(viewModel = viewModel, navController = navController, modifier = Modifier.fillMaxWidth())
        }
    }
}
