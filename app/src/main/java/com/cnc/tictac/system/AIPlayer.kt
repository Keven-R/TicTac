package com.cnc.tictac.system
import com.cnc.tictac.R
class AIPlayer(
    playerName      : String                = "AI Player",
    // Player-set username.
    playerID        : Long                  = 2L,
    // Unique player ID (system generated).
    playerIcon      : Pair<Int, String>    = Pair(R.drawable.test_avatar, "Null Player Icon"),
    // Pair for player icon: Resource, and description.
    playerAvatar    : Pair<Int, String>    = Pair(R.drawable.test_avatar, "Null Player Icon"),
    // Pair for avatar image: Resource, and description.
) : Player (playerName, playerID, playerIcon, playerAvatar) {
    override fun toString(): String {
        return "${this.playerName}: ${this.playerID}"
    }
    override fun copy() : Player {
        return HumanPlayer(playerName, playerID, playerIcon, playerAvatar)
    }
    fun generateRandomPlay(constraints : Pair<Int, Int>) : Pair<Int, Int>{
        // CURRENTLY A DUMMY METHOD
        return Pair(0, 0)
    }
}