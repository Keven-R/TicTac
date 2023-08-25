package com.cnc.tictac.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

private const val TAG = "TicTacViewModel"

class TicTacViewModel: ViewModel() {
    /* Game Driver mutable states */

    var tempEditText by mutableStateOf("")
    var tempText by mutableStateOf("")

    init {

        Log.v(TAG,"TicTacViewModel Created")
    }

    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.tempClick -> {tempEvent()}
            /**
             * Events modelled by Ryan to investigate integration with backend
             */
            // "Main Menu" From wireframe, or "HOME"
            TicTacEvent.newSinglePlayerGame -> {newSinglePlayerGame()}
            TicTacEvent.newMultiPlayerGame  -> {newMultiPlayerGame()}
        }
    }

    private fun newSinglePlayerGame(){
        // When the newSinglePlayerGame method is called, there already exists 1 player in the
        // Game driver, which is created when the user is forced to setup an account on startup.

    }
    private fun newMultiPlayerGame(){
        // When the newSinglePlayerGame method is called, there already exists 1 player in the
        // Game driver, which is created when the user is forced to setup an account on startup.

    }

    private fun tempEvent(){
        tempText = "Hi $tempEditText"
    }
}