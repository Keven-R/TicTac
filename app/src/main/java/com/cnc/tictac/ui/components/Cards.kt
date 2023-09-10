package com.cnc.tictac.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
 * @param[isHorizontal] True if displaying card horizontally
 * @param[playerName] Use's name to display
 * @param[isPlayerOne] True if player one
 * @param[avatarResourceId] Avatar resource id for player
 * @param[layoutModifier] Modifier for row or column
 * @param[avatarModifier] Modifier for avatar image
 */
@Composable
fun PlayerSelectCard (
    isHorizontal: Boolean = true,
    playerName: String = "Guest",
    isPlayerOne: Boolean = true,
    avatarResourceId: Int,
    layoutModifier: Modifier = Modifier,
    avatarModifier: Modifier = Modifier
) {
    val playerDescId = if (isPlayerOne) copy.settings_p1 else copy.settings_p2

    if (isHorizontal) {
        Row (
            modifier = layoutModifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ELEMENT: Player avatar
            AvatarBlock(
                avatarResourceId = avatarResourceId,
                isCircle = false,
                isFilled = false,
                boxModifier = avatarModifier
                    .weight(3f)
                    .fillMaxWidth()
            )

            // CONTAINER: Player info and button
            Column (
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // CONTAINER: Player info
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ELEMENT: Player 1 or 2
                    Text(
                        text = stringResource(id = playerDescId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    // ELEMENT: Player name
                    BodyLarge(
                        content = playerName,
                    )
                }

                // ELEMENT: BUTTON
                SecondaryButton(
                    label = stringResource(id = copy.settings_change_player),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TODO: ADD ACTION HERE TO GO TO SWITCH PLAYER SCREEN
                    println("change player")
                }
            }
        }
    } else {
        Column (
            modifier = layoutModifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ELEMENT: Player avatar
            AvatarBlock(
                avatarResourceId = avatarResourceId,
                isCircle = false,
                isFilled = false,
                boxModifier = avatarModifier.fillMaxWidth()
            )

            // ELEMENT: Player 1 or 2
            BodyMedium (
                content = stringResource(id = playerDescId),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            // ELEMENT: Player name
            BodyLarge(
                content = playerName,
                rowModifier = Modifier.fillMaxWidth(),
                isCenter = true
            )

            // ELEMENT: BUTTON
            SecondaryButton(
                label = stringResource(id = copy.settings_change_player),
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            ) {
                // TODO: ADD ACTION HERE TO GO TO SWITCH PLAYER SCREEN
                println("change player")
            }
        }
    }
}