package com.cnc.tictac.system

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.cnc.tictac.R

class HumanPlayer(
    playerName      : String                    = "Player",
    // Player-set username.
    playerID        : Long                      = 0L,
    // Unique player ID (system generated).
    playerIcon      : Pair<Int, String>    = Pair(R.drawable.avatar, "Null Player Icon"),
    // Pair for player icon: Resource, and description.
    playerAvatar    : Pair<Int, String>    = Pair(R.drawable.avatar, "Null Player Icon"),
    // Pair for avatar image: Resource, and description.
) : Player (playerName, playerID, playerIcon, playerAvatar) {

}