package com.cnc.tictac.backend.system

abstract class Player(
    playerName  : String, // Player-set username.
    playerID    : Long, // Unique player ID (system generated).
    playerIcon  : Pair<Int, String>, // Pair for player icon: Resource, and description.
    avatar      : Pair<Int, String>, // Pair for avatar image: Resource, and description.
) {
    abstract override fun toString() : String
}