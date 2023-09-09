package com.cnc.tictac.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/* GamePlayerCard
 *
 * Knows uses:
 *  - GameScreen: Player information when game active or ended.
 *
 * @param[playerDidWin] Null if game is active, True if player won
 * @param[isPlayerTurn] Whether it's the current player's turn or not
 * @param[playerName] String of player's name
 * @param[playerAvatarId] Resource ID of player's chosen avatar
 * @param[secondsLeft] Null if not player's turn, otherwise 0-10
 */
@Composable
fun GamePlayerCard (
    playerDidWin: Boolean? = null, // TODO: From ViewModel
    isPlayerTurn: Boolean? = null, // TODO: From ViewModel
    playerName: String, // TODO: From ViewModel
    playerAvatarId: String, // TODO: From ViewModel
    secondsLeft: Int? = null, // TODO: From ViewModel
    modifier: Modifier = Modifier.fillMaxWidth()
) {

}

//@Preview
//@Composable
//fun GamePlayerCardPreview () {
//    Box(modifier = Modifier.fillMaxSize()) {
//        Row(modifier = Modifier.fillMaxWidth()) {
//            GamePlayerCard(
//                playerDidWin =,
//                isPlayerTurn =,
//                playerName =,
//                playerAvatarId =,
//                secondsLeft =,
//            )
//        }
//    }
//}

/* MarkerDiagram
 *
 * Knows uses:
 *  - ProfileScreen: win ratio graphic
 *  - GameScreen: win ratio graphic
 */
@Composable
fun MarkerGraphics (
) {

}