package com.cnc.tictac.system

import android.graphics.drawable.Drawable

abstract class Player(
    playerName  : String, // Player-set username.
    playerID    : Long, // Unique player ID (system generated).
    playerIcon  : Pair<Int, String>, // Pair for player icon: Resource, and description.
    avatar      : Pair<Int, String>, // Pair for avatar image: Resource, and description.
) {
}