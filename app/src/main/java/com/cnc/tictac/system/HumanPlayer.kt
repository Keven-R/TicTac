package com.cnc.tictac.system

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.cnc.tictac.R

class HumanPlayer(
    var playerName      : String                    = "Player",
    // Player-set username.
    var playerID        : Long                      = 0L,
    // Unique player ID (system generated).
    var playerIcon      : Pair<Int, String>    = Pair(R.drawable.avatar, "Null Player Icon"),
    // Pair for player icon: Resource, and description.
    var playerAvatar    : Pair<Int, String>    = Pair(R.drawable.avatar, "Null Player Icon"),
    // Pair for avatar image: Resource, and description.
) : Player (playerName, playerID, playerIcon, playerAvatar) {
    override fun toString(): String {
        return "${this.playerName}: ${this.playerID}"
    }

}