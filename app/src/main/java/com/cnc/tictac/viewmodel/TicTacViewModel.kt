package com.cnc.tictac.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cnc.tictac.R
import com.cnc.tictac.backend.database.PLAYER_ROOM_DATABASE
import com.cnc.tictac.backend.gamedriver.GameConfig
import com.cnc.tictac.backend.gamedriver.GameDriver
import com.cnc.tictac.backend.system.AIPlayer
import com.cnc.tictac.backend.system.HumanPlayer

private const val TAG = "TicTacViewModel"
private const val TYPE = "EVENT: "

enum class PLAYERWINSTATUS { LOSS, DRAW, WIN }

enum class MENU { RUNNING, PAUSE, RESTART, EXIT, UNDO }

class TicTacViewModel() : ViewModel(){
    val avatarArray = arrayOf(R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
        R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7,
        R.drawable.avatar_8, R.drawable.avatar_9, R.drawable.avatar_10)

    var users by mutableStateOf(listOf(
        HumanPlayer("Ryan",null,"O", R.drawable.avatar_8),
        HumanPlayer("Jasmine",null,"O", R.drawable.avatar_1),
        HumanPlayer("Keven",null,"O", R.drawable.avatar_3),
        HumanPlayer("Sajib",null,"O", R.drawable.avatar_3),
        HumanPlayer("Kathy",null,"O", R.drawable.avatar_6),
        HumanPlayer("John",null,"O", R.drawable.avatar_2),
        HumanPlayer("Lilly",null,"O", R.drawable.avatar_10)
    ))

    var gd : GameDriver? = null
    var db : PLAYER_ROOM_DATABASE? = null

    /* Player 1 States */
    var player1 by mutableStateOf("Annie")
    var player1Timer by mutableIntStateOf(9)
    var player1Turn by mutableStateOf(true)
    var player1WinStatus by mutableStateOf(PLAYERWINSTATUS.DRAW)
    var player1Avatar by mutableIntStateOf(R.drawable.avatar_5)
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
    var boardState by mutableStateOf(arrayOf<String>())
    var gameActive by mutableStateOf(true) // This and gameEnded could probably be the same
    var gameEnded by mutableStateOf(false)
    var startingSelection by mutableIntStateOf(0) // 0 = "Player 1", 1 = "Player 2"
    var boardSelection by mutableIntStateOf(0) // 0 = 3x3, 1 = 4x4, 2 = 5x5
    var winConditionSelection by mutableIntStateOf(0) // 0 = 3, 1 = 4, 2 = 5
    var winSelectable by mutableStateOf(arrayOf(false, true, true)) // Controls button selection
    var undoAvailable by mutableStateOf(false)
    var winIndices by mutableStateOf(emptyArray<Boolean>()) // Fill with win when happens

    /* UI States*/
    var gameUIState by mutableStateOf(MENU.RUNNING)
    var newUser by mutableStateOf(false)
    var player1Edit by mutableStateOf(true)
    var selectedAvatar by mutableIntStateOf(findAvatar())
    var playerTextFieldValue by mutableStateOf(findEditTextValue())
    var playerSwitchUI by mutableStateOf(true)
    var userSelectIndex by mutableIntStateOf(1)

    init {
        Log.v(TAG,"TicTacViewModel Created")
        Log.v("Test", "Test Log Working")
    }

    // is keyword for when its a dataclass and takes parameters (can be on all of them but helps separate them)
    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.TempEvent -> Log.v(TAG, TYPE+"Temp Event For Testing")
            TicTacEvent.StartGame -> gameStart()
            TicTacEvent.NewSinglePlayerGame -> newSinglePlayerGame()
            TicTacEvent.NewMultiPlayerGame  -> newMultiPlayerGame()
            TicTacEvent.ProfileMenuSelect -> profileMenuSelect()
            TicTacEvent.Undo -> undo()
            TicTacEvent.Restart -> restart()
            TicTacEvent.Exit -> exit()
            TicTacEvent.SaveUser -> saveUser()
            TicTacEvent.TimerStart -> timerStart()
            TicTacEvent.TimerStop -> timerStop()
            is TicTacEvent.MarkerPlaced -> markerPlaced(event.position)
        }
    }

    /*
        ** OnEvent functions **
    */

    /********************************
     * gameStart()
     * Handles setting up single or multiplayer game.
     *  > Board size.
     *  > Win length.
     *  > Player markers.
     *  > Who goes first.
     *  Doesnt create game driver, only updates game driver with new configuration
     *******************************/
    private fun gameStart(){
        Log.v(TAG, TYPE+"StartGame")
        var boardSize   : Int = 3
        var winSize     : Int = 3
        Log.v(TAG, TYPE+"Obtaining board size selection")
        /**
         * Sets the player icons "x" or "o" inside Player objects for the first and second player.
         * The first player selects their icon 0:x 1:o, and it sets the alternate icon for the second
         * player.
         */
        when(player1Marker){
            0 -> {
                this.gd!!.getPlayerArray()[0]!!.playerIcon = "x"
                this.gd!!.getPlayerArray()[1]!!.playerIcon = "o"
            }
            1 -> {
                this.gd!!.getPlayerArray()[0]!!.playerIcon = "o"
                this.gd!!.getPlayerArray()[1]!!.playerIcon = "x"
            }
        }
        /**
         * Modifies the board width and height variable on the GameConfig class
         * backend supports rectangular boards - this is obfuscated in the frontend.
         * **/
        when(boardSelection){
            0 -> { boardSize = 3; boardState = Array(9) { _ -> "" } }
            1 -> { boardSize = 4; boardState = Array(16) { _ -> "" } }
            2 -> { boardSize = 5; boardState = Array(25) { _ -> "" } }
        }
        Log.v(TAG, TYPE+"Obtaining board win condition selection.")
        /**
         * Modifies the wincondition variable on the GameConfig class
         * **/
        when(winConditionSelection){
            0 -> winSize = 3
            1 -> winSize = 4
            2 -> winSize = 5
        }
        /**
         * Changes whether "firstPlayer" or "secondPlayer" plays first by toggling setting the
         * value of currentPlayer in GameDriver to 1 or 0 initially. Players are stored in an array
         * of length 2 inside GameDriver.
         * **/
        when(startingSelection){
            0 -> this.gd!!.setPlayerOrder(0)
            1 -> this.gd!!.setPlayerOrder(1)
        }
        Log.v(TAG, TYPE+"Building configuration.")
        var config = GameConfig(boardSize, boardSize, winSize)
        Log.v(TAG, TYPE+"Updating game driver with new config.")
        /**
         * game driver is not created here- only updated with new configuration.
         * **/
        this.gd!!.reinit(config)
    }
    private fun newSinglePlayerGame(){
        Log.v(TAG, TYPE+"NewSinglePlayerGame")
        this.gd = GameDriver(GameConfig(), this.db)
        this.gd!!.setFirstPlayer(HumanPlayer()) // DUMMY FOR NOW
        this.gd!!.setSecondPlayer(AIPlayer())
    }
    private fun newMultiPlayerGame(){
        Log.v(TAG, TYPE+"NewMultiplayerPlayerGame")
        this.gd = GameDriver(GameConfig(), this.db)
        this.gd!!.setFirstPlayer(HumanPlayer()) // DUMMY FOR NOW
        this.gd!!.setSecondPlayer(HumanPlayer()) // DUMMY FOR NOW
    }
    private fun profileMenuSelect(){Log.v(TAG, TYPE+"ProfileMenuSelect")}
    private fun undo(){Log.v(TAG, TYPE+"Undo")}
    private fun restart(){Log.v(TAG, TYPE+"Restart")}
    private fun exit(){Log.v(TAG, TYPE+"Exit")}
    private fun saveUser(){Log.v(TAG, TYPE+"SaveUser")}
    private fun timerStart(){Log.v(TAG, TYPE+"TimerStart")}
    private fun timerStop(){Log.v(TAG, TYPE+"TimerStop")}
    private fun markerPlaced(position: Int){Log.v(TAG, TYPE+"MarkerPlaced: Position = " + position)}

    /*
        ** Helper functions **
    */
    fun getMarkerSymbol(stateMarker: Int): String{
        return if(stateMarker == 0){
            "X"
        } else{
            "O"
        }
    }

    fun getBoardSize(): Int{
        return when(boardSelection){
            0 -> 3
            1 -> 4
            2 -> 5
            else -> {3}
        }
    }

    private fun findAvatar(): Int{
        if(newUser){
            return 0
        }
        var currentAvatar = 0
        if(player1Edit){ currentAvatar = player1Avatar }else{ player2Avatar }

        for((index, avatar) in avatarArray.withIndex()){
            if(avatar == currentAvatar){
                return index
            }
        }
        return 0
    }

    fun findEditTextValue(): String{
        return when(player1Edit){
            true -> {if(newUser){
                ""
            }else{
                player1
            }}

            false -> {if(newUser){
                ""
            }else{
                player2
            }}
        }
    }
}