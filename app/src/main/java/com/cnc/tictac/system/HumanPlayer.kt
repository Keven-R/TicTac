package com.cnc.tictac.system

import android.graphics.drawable.Drawable
class HumanPlayer(
    playerName      : String                    = "Player",
                    // Player-set username.
    playerID        : Long                      = 0L,
                    // Unique player ID (system generated).
    playerIcon      : Pair<Drawable, String>    = Pair(res.drawable.avatar, "Null Player Icon"),
                    // Pair for player icon: Resource, and description.
    playerAvatar    : Pair<Drawable, String>    = Pair(res.drawable.avatar, "Null Player Icon"),
                    // Pair for avatar image: Resource, and description.
) : Player (playerName, playerID, playerIcon, playerAvatar) {

}