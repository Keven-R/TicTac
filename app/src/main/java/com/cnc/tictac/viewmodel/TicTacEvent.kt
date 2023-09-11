package com.cnc.tictac.viewmodel

sealed class TicTacEvent{
    /**
     * Ryan is mocking events here to see how complete the backend is.
     * Some of these events may be combined, removed, etc.. during later production.
     */

    /*
    User clicks the "single player" or "multiplayer" buttons on the HOME screen
     */
    object NewSinglePlayerGame: TicTacEvent()
    object NewMultiPlayerGame: TicTacEvent()
    object ProfileMenuSelect: TicTacEvent()
}
