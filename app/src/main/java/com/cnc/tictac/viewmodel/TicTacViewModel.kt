package com.cnc.tictac.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cnc.tictac.R

private const val TAG = "TicTacViewModel"
private const val TYPE = "EVENT: "

class TicTacViewModel: ViewModel() {

    /* Mutable states */
    var player1 by mutableStateOf("Jasmine")
    var player1Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player2 by mutableStateOf("AI")
    var player2Avatar by mutableIntStateOf(R.drawable.avatar_2)
    var player1Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var startingSelection by mutableIntStateOf(0) // 0 = "Player 1", 1 = "Player 2"
    var boardSelection by mutableIntStateOf(0) // 0 = 3x3, 1 = 4x4, 2 = 5x5
    var winConditionSelection by mutableIntStateOf(0) // 0 = 3, 1 = 4, 2 = 5
    var winSelectable by mutableStateOf(arrayOf(false, true, true)) // Controls button selection



    init {
        Log.v(TAG,"TicTacViewModel Created")
    }

    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.NewSinglePlayerGame -> {Log.v(TAG, TYPE+"NewSinglePlayerGame")}
            TicTacEvent.NewMultiPlayerGame  -> {Log.v(TAG, TYPE+"NewMultiplayerPlayerGame")}
            TicTacEvent.ProfileMenuSelect -> {Log.v(TAG, TYPE+"ProfileMenuSelect")}
        }
    }
}