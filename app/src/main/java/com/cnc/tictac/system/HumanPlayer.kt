package com.cnc.tictac.system
import com.cnc.tictac.R
class HumanPlayer(
    playerName      : String                = "Player",
    // Player-set username.
    playerID        : Long                  = 1L,
    // Unique player ID (system generated).
    playerIcon      : Pair<Int, String>    = Pair(R.drawable.avatar, "Null Player Icon"),
    // Pair for player icon: Resource, and description.
    playerAvatar    : Pair<Int, String>    = Pair(R.drawable.avatar, "Null Player Icon"),
    // Pair for avatar image: Resource, and description.
) : Player (playerName, playerID, playerIcon, playerAvatar) {
    override fun toString(): String {
        return "${this.playerName}: ${this.playerID}"
    }
    override fun copy() : Player {
        return HumanPlayer(playerName, playerID, playerIcon, playerAvatar)
    }
}