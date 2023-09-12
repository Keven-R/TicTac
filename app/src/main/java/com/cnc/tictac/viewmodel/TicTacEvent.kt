package com.cnc.tictac.viewmodel

sealed class TicTacEvent{
    object NewSinglePlayerGame: TicTacEvent()
    object NewMultiPlayerGame: TicTacEvent()
    object ProfileMenuSelect: TicTacEvent()
    object PauseGame: TicTacEvent()
    object Undo: TicTacEvent()
    object Restart: TicTacEvent()
    object Exit: TicTacEvent()
    data class SaveUser(val name: String, val avatar: Int): TicTacEvent()
}
