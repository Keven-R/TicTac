package com.cnc.tictac.ui.screens

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.GameBoard
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
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        when (deviceInfo.screenHeightType) {
            // Mobile landscape layout
            is DeviceInfo.DeviceType.Compact -> {
                DisplayGameScreenLandscapeMobile(navController, viewModel)
            }
            else -> {
                DisplayGameScreenLandscape(navController, viewModel)
            }
        }
    } else {
        when (deviceInfo.screenWidthType) {
            // Mobile portrait layout
            is DeviceInfo.DeviceType.Compact -> {
                DisplayGameScreenPortraitMobile(navController, viewModel)
            } else -> {
                DisplayGameScreenPortrait(navController, viewModel)
            }
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
                .fillMaxSize()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // CONTAINER: Left side
            Column (
                modifier = Modifier.fillMaxHeight().weight(3f).fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // CONTAINER: Game player cards
                Column (
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    // ELEMENT: player 1
                    // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
                    GamePlayerCard(
                        modifier = Modifier,
                        isRowLayout = true,

                        playerName = "jasmine",                         // TODO: ADD player name
                        playerAvatarResourceId = R.drawable.avatar_7,   // TODO: ADD player resource id
                        playerMarker = "x",                             // TODO: ADD player marker
                        isGameEnded = false,                            // TODO: Game status

                        playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: REQUIRED IF game ended

                        isPlayerTurn = true,                            // TODO: OPTIONAL, required if game ongoing
                        secondsLeft = 8,                                // TODO: OPTPONAL, required if game ongoing
                    )

                    // ELEMENT: player 2
                    // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
                    GamePlayerCard(
                        modifier = Modifier,
                        isRowLayout = true,

                        playerName = "guest",                           // TODO: ADD player name
                        playerAvatarResourceId = R.drawable.avatar_2,   // TODO: ADD player resource id
                        playerMarker = "o",                             // TODO: ADD player marker
                        isGameEnded = false,                            // TODO: Game status

                        playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: OPTIONAL, required if game ended

                        isPlayerTurn = false,                           // TODO: OPTIONAL, required if game ongoing
                        secondsLeft = 10,                               // TODO: OPTPONAL, required if game ongoing
                    )
                }

                // ELEMENT: all menu controls
                // TODO: remove "enableUndo" arg if undo is available to use
                GameMenuButtonGroup(enableUndo = false, modifier = Modifier.fillMaxWidth())
            }

            // CONTAINER: right side (has game board)
            Column(
                modifier = Modifier.fillMaxHeight().weight(2f).fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // TODO: VIEWMODEL -> pls add args, see GameBoard.kt docs
                GameBoard(
                    modifier = Modifier.fillMaxHeight(),
                    isGameActive = true,                                        // TODO: REQUIRED arg
                    boardSize = 3,                                              // TODO: REQUIRED arg
                    board = arrayOf("o", "x", "x", "", "o", "", "", "", "o"),   // TODO: REQUIRED arg
                    // TODO: OPTIONAL arg, only add if game is finished
                    // winIndices = arrayOf(true, false, false, false, true, false, false, false, true),
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // CONTAINER: Left side (game player cards)
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // ELEMENT: player 1
                // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
                GamePlayerCard(
                    modifier = Modifier,
                    isRowLayout = true,

                    playerName = "jasmine",                         // TODO: ADD player name
                    playerAvatarResourceId = R.drawable.avatar_7,   // TODO: ADD player resource id
                    playerMarker = "x",                             // TODO: ADD player marker
                    isGameEnded = false,                            // TODO: Game status

                    playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: REQUIRED IF game ended

                    isPlayerTurn = true,                            // TODO: OPTIONAL, required if game ongoing
                    secondsLeft = 8,                                // TODO: OPTPONAL, required if game ongoing
                )

                // ELEMENT: player 2
                // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
                GamePlayerCard(
                    modifier = Modifier,
                    isRowLayout = true,

                    playerName = "guest",                           // TODO: ADD player name
                    playerAvatarResourceId = R.drawable.avatar_2,   // TODO: ADD player resource id
                    playerMarker = "o",                             // TODO: ADD player marker
                    isGameEnded = false,                            // TODO: Game status

                    playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: OPTIONAL, required if game ended

                    isPlayerTurn = false,                           // TODO: OPTIONAL, required if game ongoing
                    secondsLeft = 10,                               // TODO: OPTPONAL, required if game ongoing
                )
            }

            // CONTAINER: right side (game board and menu)
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // CONTAINER: game board
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.End,
                ) {
                    // TODO: VIEWMODEL -> pls add args, see GameBoard.kt docs
                    GameBoard(
                        modifier = Modifier.fillMaxWidth(),
                        isGameActive = true,                                        // TODO: REQUIRED arg
                        boardSize = 3,                                              // TODO: REQUIRED arg
                        board = arrayOf("o", "x", "x", "", "o", "", "", "", "o"),   // TODO: REQUIRED arg
                        // TODO: OPTIONAL arg, only add if game is finished
                        // winIndices = arrayOf(true, false, false, false, true, false, false, false, true),
                    )
                }

                // ELEMENT: all menu controls
                // TODO: remove "enableUndo" arg if undo is available to use
                GameMenuButtonGroup(enableUndo = false, modifier = Modifier.fillMaxWidth())
            }
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
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                // TODO: VIEWMODEL -> pls add args, see GameBoard.kt docs
                GameBoard(
                    modifier = Modifier.fillMaxWidth(),
                    isGameActive = true,                                        // TODO: REQUIRED arg
                    boardSize = 3,                                              // TODO: REQUIRED arg
                    board = arrayOf("o", "x", "x", "", "o", "", "", "", "o"),   // TODO: REQUIRED arg
                    // TODO: OPTIONAL arg, only add if game is finished
                    // winIndices = arrayOf(true, false, false, false, true, false, false, false, true),
                )
            }

            // ELEMENT: player 2
            // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
            GamePlayerCard(
                modifier = Modifier.fillMaxWidth(),
                isRowLayout = true,

                playerName = "guest",                           // TODO: ADD player name
                playerAvatarResourceId = R.drawable.avatar_2,   // TODO: ADD player resource id
                playerMarker = "o",                             // TODO: ADD player marker
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
                // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
                GamePlayerCard(
                    isRowLayout = true,

                    playerName = "jasmine",                         // TODO: ADD player name
                    playerAvatarResourceId = R.drawable.avatar_7,   // TODO: ADD player resource id
                    playerMarker = "x",                             // TODO: ADD player marker
                    isGameEnded = false,                            // TODO: Game status

                    playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: REQUIRED IF game ended

                    isPlayerTurn = true,                            // TODO: OPTIONAL, required if game ongoing
                    secondsLeft = 8,                                // TODO: OPTPONAL, required if game ongoing
                )

                // ELEMENT: player 2
                // TODO: VIEWMODEL -> pls add args, see Cards.kt docs
                GamePlayerCard(
                    isRowLayout = true,
                    inverse = true,

                    playerName = "guest",                           // TODO: ADD player name
                    playerAvatarResourceId = R.drawable.avatar_2,   // TODO: ADD player resource id
                    playerMarker = "o",                             // TODO: ADD player marker
                    isGameEnded = false,                            // TODO: Game status

                    playerWinStatus = PLAYERWINSTATUS.DRAW,         // TODO: OPTIONAL, required if game ended

                    isPlayerTurn = false,                           // TODO: OPTIONAL, required if game ongoing
                    secondsLeft = 10,                               // TODO: OPTPONAL, required if game ongoing
                )
            }

            // CONTAINER: game board
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                // TODO: VIEWMODEL -> pls add args, see GameBoard.kt docs
                GameBoard(
                    modifier = Modifier.fillMaxWidth(),
                    isGameActive = true,                                        // TODO: REQUIRED arg
                    boardSize = 3,                                              // TODO: REQUIRED arg
                    board = arrayOf(
                        "o",
                        "x",
                        "x",
                        "",
                        "o",
                        "",
                        "",
                        "",
                        "o"
                    ),   // TODO: REQUIRED arg
                    // TODO: OPTIONAL arg, only add if game is finished
                    // winIndices = arrayOf(true, false, false, false, true, false, false, false, true),
                )
            }

            // CONTAINER: all menu controls
            // TODO: remove "enableUndo" arg if undo is available to use
            GameMenuButtonGroup(enableUndo = false, modifier = Modifier.fillMaxWidth())
        }
    }
}
