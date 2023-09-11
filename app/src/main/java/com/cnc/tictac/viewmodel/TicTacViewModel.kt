package com.cnc.tictac.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "TicTacViewModel"
private const val TYPE = "EVENT: "

class TicTacViewModel: ViewModel() {

    /* Mutable states */
//    var tempEditText by mutableStateOf("")
//    var tempText by mutableStateOf("")

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