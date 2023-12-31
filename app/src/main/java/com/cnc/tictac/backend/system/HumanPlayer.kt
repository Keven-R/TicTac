package com.cnc.tictac.backend.system
import com.cnc.tictac.backend.database.PlayerSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PlayerSerializer::class)
class HumanPlayer(
    playerName      : String    = "Player",
    // Player-set username.
    playerID        : Int?      = null,
    // Unique player ID (system generated).
    playerIcon      : String    = "D",
    // Pair for player icon: Resource, and description.
    playerAvatar    : Int?      = null,
    // Pair for avatar image: Resource, and description.
) : Player (playerName, playerID, playerIcon, playerAvatar) {
    init{
        if(playerID == null)
            this.generateUniqueID()
    }
    private fun generateUniqueID(){
        var intRep = 0
        val charArr = this.playerName.toCharArray()
        for(char in charArr){
            intRep += char.hashCode()
        }
        val random = (0..1000).random()
        this.playerID = random + intRep
    }
    override fun toString(): String {
        return "${this.playerName}: ${this.playerID}"
    }
    override fun copy() : Player {
        return HumanPlayer(playerName, playerID, playerIcon, playerAvatar)
    }
    override fun equals(player : Any?) : Boolean {
        return (player is Player) && (player.playerName == this.playerName && player.playerID == this.playerID)
    }
}