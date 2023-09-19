package com.cnc.tictac.viewmodel

import com.cnc.tictac.backend.system.HumanPlayer

sealed class TicTacEvent{
    object TempEvent: TicTacEvent()
    object StartGame: TicTacEvent()
    object NewSinglePlayerGame: TicTacEvent()
    object NewMultiPlayerGame: TicTacEvent()
    object ProfileMenuSelect: TicTacEvent()
    object Undo: TicTacEvent()
    object Restart: TicTacEvent()
    object Exit: TicTacEvent()
    object SaveUser: TicTacEvent()
    object TimerStop: TicTacEvent()
    object TimerStart: TicTacEvent()
    data class ChangePlayer(val player: HumanPlayer): TicTacEvent()
    data class MarkerPlaced(val position: Int): TicTacEvent()
}
