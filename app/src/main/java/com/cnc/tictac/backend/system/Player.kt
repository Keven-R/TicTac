package com.cnc.tictac.backend.system

import com.cnc.tictac.backend.database.PlayerSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PlayerSerializer::class)
abstract class Player(
    var playerName      : String, // Player-set username.
    var playerID        : Int?, // Unique player ID (system generated).
    var playerIcon      : String, // Pair for player icon: Resource, and description.
    var playerAvatar    : Int?, // Pair for avatar image: Resource, and description.
) {
    abstract override fun toString() : String
    abstract fun copy() : Player

    abstract fun equals(player : Player?) : Boolean
}