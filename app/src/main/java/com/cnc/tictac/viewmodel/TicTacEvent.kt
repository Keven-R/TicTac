package com.cnc.tictac.viewmodel

sealed class TicTacEvent{
    object TempEvent: TicTacEvent()
    object NewSinglePlayerGame: TicTacEvent()
    object NewMultiPlayerGame: TicTacEvent()
    object ProfileMenuSelect: TicTacEvent()
    object Undo: TicTacEvent()
    object Restart: TicTacEvent()
    object Exit: TicTacEvent()
    object SaveUser: TicTacEvent()
    object TimerStop: TicTacEvent()
    object TimerStart: TicTacEvent()
    data class MarkerPlaced(val position: Int): TicTacEvent()
}
