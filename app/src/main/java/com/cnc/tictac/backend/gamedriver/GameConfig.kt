package com.cnc.tictac.backend.gamedriver
data class GameConfig(
    // Board Configurations
    val boardHeight     : Int = 3,
    val boardWidth      : Int = 3,
    val boardMinimumWin : Int = 3,
)