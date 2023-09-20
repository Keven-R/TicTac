package com.cnc.tictac.viewmodel;

import android.util.Log

private const val TAG = "StateMachine"

enum class STATE {
    OUTSIDE_GAME,
    PLAYER1_TURN,
    PLAYER2_TURN,
    SEARCHING_P1_WIN,
    SEARCHING_P2_WIN,
    GAME_OVER,
    ERROR
}
fun STATE?.ChangeState(stateTo : STATE?) : STATE? {
    Log.d(TAG, "$this -> $stateTo")
    if(this is STATE && stateTo is STATE){
        return stateTo
    } else {
        return null
    }
}