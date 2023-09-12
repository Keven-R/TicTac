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

enum class PLAYERWINSTATUS { LOSS, DRAW, WIN }

class TicTacViewModel: ViewModel() {

    /* Player 1 States */
    var player1 by mutableStateOf("Jasmine")
    var player1Timer by mutableIntStateOf(9)
    var player1Turn by mutableStateOf(true)
    var player1WinStatus by mutableStateOf(PLAYERWINSTATUS.DRAW)
    var player1Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player1Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player1StatMarker by mutableStateOf("ooo/////xx")
    var player1WinString by mutableStateOf("wins: 8 (33%)")    // These stat strings might need to be
    var player1DrawString by mutableStateOf("draws: 13 (54%)") // processed depending on backend
    var player1LossesString by mutableStateOf("losses: 3 (13%)")
    var player1TotalGamesString by mutableStateOf("total games 24")

    /* Player 2 States */
    var player2 by mutableStateOf("AI")
    var player2Timer by mutableIntStateOf(12)
    var player2Turn by mutableStateOf(false)
    var player2WinStatus by mutableStateOf(PLAYERWINSTATUS.DRAW)
    var player2Avatar by mutableIntStateOf(R.drawable.avatar_2)
    var player2Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
//    var player2StatMarker by mutableStateOf("ooo/////xx")
//    var player2WinString by mutableStateOf("wins: 8 (33%)")    // These stat strings might need to be
//    var player2DrawString by mutableStateOf("draws: 13 (54%)") // processed depending on backend
//    var player2LossesString by mutableStateOf("losses: 3 (13%)")
//    var player2TotalGamesString by mutableStateOf("total games 24")

    /* Game States */
    var boardState by mutableStateOf(arrayOf("o", "x", "x", "", "o", "", "", "o", ""))
    var gameActive by mutableStateOf(true) // This and gameEnded could probably be the same
    var gameEnded by mutableStateOf(false)
    var startingSelection by mutableIntStateOf(0) // 0 = "Player 1", 1 = "Player 2"
    var boardSelection by mutableIntStateOf(0) // 0 = 3x3, 1 = 4x4, 2 = 5x5
    var winConditionSelection by mutableIntStateOf(0) // 0 = 3, 1 = 4, 2 = 5
    var winSelectable by mutableStateOf(arrayOf(false, true, true)) // Controls button selection
    var undoAvailable by mutableStateOf(false)
    var winIndices by mutableStateOf(emptyArray<Boolean>()) // Fill with win when happens

    /* UI States*/
    var newUser by mutableStateOf(false)
    var player1Edit by mutableStateOf(true)

    init {
        Log.v(TAG,"TicTacViewModel Created")
    }

    // is keyword for when its a dataclass and takes parameters (can be on all of them but helps separate them)
    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.NewSinglePlayerGame -> {Log.v(TAG, TYPE+"NewSinglePlayerGame")}
            TicTacEvent.NewMultiPlayerGame  -> {Log.v(TAG, TYPE+"NewMultiplayerPlayerGame")}
            TicTacEvent.ProfileMenuSelect -> {Log.v(TAG, TYPE+"ProfileMenuSelect")}
            TicTacEvent.PauseGame -> {Log.v(TAG, TYPE+"PauseGame")}
            TicTacEvent.Undo -> {Log.v(TAG, TYPE+"Undo")}
            TicTacEvent.Restart -> {Log.v(TAG, TYPE+"Restart")}
            TicTacEvent.Exit -> {Log.v(TAG, TYPE+"Exit")}
            is TicTacEvent.SaveUser -> {Log.v(TAG, TYPE+"SaveUser? name= " + event.name + " avatar: " + event.avatar)}
        }
    }

    // Helper function to turn player1 Marker state to String
    fun getMarkerSymbol(stateMarker: Int): String{
        return if(stateMarker == 0){
            "X"
        } else{
            "O"
        }
    }

    // Helper function to turn board size state to proper value
    fun getBoardSize(): Int{
        return when(boardSelection){
            0 -> 3
            1 -> 4
            2 -> 5
            else -> {3}
        }
    }
}