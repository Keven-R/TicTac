package com.cnc.tictac.ui.components

import androidx.compose.runtime.Composable

/* GameBoard
 *
 * Knows uses:
 *  - GameScreen: Displayes game board
 *
 * REQUIRED PARAMS
 * @param[isRowLayout] Layout of card
 * @param[playerName] String of player's name
 * @param[playerAvatarResourceId] Resource ID of player's chosen avatar
 * @param[playerMarker] "x" or "o
 * @param[isGameEnded] True if game completed, false if ongoing
 *
 * OPTIONAL PARAMS
 * -- Game is ongoing
 * @param[isPlayerTurn] True if player turn, else false
 * @param[secondsLeft] 0 by default (not player's turn), otherwise 0-10
 *
 * -- Game is ended
 * @param[playerWinStatus] Win/loss/draw status for that player. See enum below.
 */
@Composable
fun GameBoard (
    board: Array<IntArray>

) {
}