package com.cnc.tictac.backend.system
class AIPlayer(
    playerName      : String                = "AI Player",
    // Player-set username.
    playerID        : Int?                   = null,
    // Unique player ID (system generated).
    playerIcon      : Pair<String, String>     = Pair("", "Null Player Icon"),
    // Pair for player icon: Resource, and description.
    playerAvatar    : Pair<String, String>     = Pair("", "Null Player Icon"),
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
    fun generateRandomPlay(constraints : Pair<Int, Int>) : Pair<Int, Int>{
        // CURRENTLY A DUMMY METHOD
        return Pair(0, 0)
    }
    override fun equals(player : Player?) : Boolean {
        if (player?.playerName == this.playerName && player?.playerID == this.playerID) {
            return true
        }
        return false
    }
}