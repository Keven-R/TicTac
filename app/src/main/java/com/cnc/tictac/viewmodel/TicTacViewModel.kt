package com.cnc.tictac.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

private const val TAG = "TicTacViewModel"

class TicTacViewModel: ViewModel() {

    var tempEditText by mutableStateOf("")
    var tempText by mutableStateOf("")

    init {
        Log.v(TAG,"TicTacViewModel Created")
    }

    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.tempClick -> {tempEvent()}
        }
    }

    private fun tempEvent(){
        tempText = "Hi $tempEditText"
    }
}