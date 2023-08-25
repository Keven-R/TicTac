package com.cnc.tictac.viewmodel

sealed class TicTacEvent{
    object tempClick: TicTacEvent()

    /**
     * Ryan is mocking events here to see how complete the backend is.
     * Some of these events may be combined, removed, etc.. during later production.
     */

    /*
    User clicks the "single player" or "multiplayer" buttons on the HOME screen
     */
    object newSinglePlayerGame: TicTacEvent()
    object newMultiPlayerGame: TicTacEvent()

}
