package com.cnc.tictac.backend.system

import kotlin.random.Random

class AIPlayer(
    playerName      : String    = "AI Player",
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
    fun generateRandomPlay(constraints : Pair<Int, Int>) : Pair<Int, Int>{
        val x = Random.nextInt(0, constraints.first)
        val y = Random.nextInt(0, constraints.second)
        return Pair(x, y)
    }
    override fun equals(player : Any?) : Boolean {
        return (player is Player) && (player.playerName == this.playerName && player.playerID == this.playerID)
    }
}