package com.cnc.tictac.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.cnc.tictac.R.drawable as images
import com.cnc.tictac.R.string as copy

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

/* MarkerGraphics
 *
 * Knows uses:
 *  - ProfileScreen: win ratio graphic
 *  - GameScreen: timer
 *
 * @param[content] 10 char long string to display e.g. "ooo/////xx"
 * @param[transparentIndex] String index of when transparency begins (timer).
 * @param[rowModifier] Modifier for row
 * @param[textModifier] Modifier for text
 */
@Composable
fun MarkerGraphics (
    content: String,
    transparentIndex: Int = 10,
    alignCenter : Boolean = true,
    rowModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    // Center align fo profile, start for game screen.
    val rowAlignment = if (alignCenter) Alignment.CenterHorizontally else Alignment.Start

    Row (
        modifier = rowModifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = rowAlignment)
    ) {
        for (currIndex in content.indices) {
            // Add transparency for timer
            if (currIndex >= transparentIndex) {
                Text(
                    text = content[currIndex].toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = textModifier.alpha(0.16f),
                )
            // Default styling
            } else {
                Text(
                    text = content[currIndex].toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = textModifier,
                )
            }
        }
    }
}

/* PlayerSelectCard
 *
 * Knows uses:
 *  - MultiplayerSettingsScreen
 *
 * @param[content] 10 char long string to display e.g. "ooo/////xx"
 * @param[transparentIndex] String index of when transparency begins (timer).
 * @param[rowModifier] Modifier for row
 * @param[textModifier] Modifier for text
 */
@Composable
fun PlayerSelectCard (
    playerName: String = "Guest",
    isPlayerOne: Boolean = true,
    avatarResourceId: Int = images.avatar_1,
    contentDescriptionId: Int = copy.avatar,
) {

}