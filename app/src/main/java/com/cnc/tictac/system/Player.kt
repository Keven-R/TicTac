package com.cnc.tictac.system
abstract class Player(
    val playerName      : String, // Player-set username.
    val playerID        : Long, // Unique player ID (system generated).
    val playerIcon      : Pair<Int, String>, // Pair for player icon: Resource, and description.
    val playerAvatar    : Pair<Int, String>, // Pair for avatar image: Resource, and description.
) {
    abstract override fun toString() : String
    abstract fun copy() : Player
}