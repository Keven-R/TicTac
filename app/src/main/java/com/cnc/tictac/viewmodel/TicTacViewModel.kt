package com.cnc.tictac.viewmodel

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.cnc.tictac.R
import com.cnc.tictac.backend.database.PLAYER_ROOM_DATABASE
import com.cnc.tictac.backend.gamedriver.GameConfig
import com.cnc.tictac.backend.gamedriver.GameDriver
import com.cnc.tictac.backend.system.AIPlayer
import com.cnc.tictac.backend.system.HumanPlayer
import com.cnc.tictac.backend.system.Player
import com.cnc.tictac.backend.system.WinCondition
import kotlin.math.abs
import kotlin.random.Random

private const val TAG = "TicTacViewModel"
private const val TYPE = "EVENT: "

enum class PLAYERWINSTATUS { LOSS, DRAW, WIN }
enum class UIPLAYERSELECT { PLAYER1, PLAYER2 }
enum class MENU { RUNNING, PAUSE, RESTART, EXIT, UNDO }

class TicTacViewModel(context: Context) : ViewModel(){

    /* For establishing some initial UI states */
    val avatarArray = arrayOf(R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
        R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7,
        R.drawable.avatar_8, R.drawable.avatar_9, R.drawable.avatar_10)

    /* Backend Variables */
    private var placeHolderHumanPlayer: Player = HumanPlayer()
    private var placeHolderAIPlayer: Player = AIPlayer()
    var gd : GameDriver
    var db : PLAYER_ROOM_DATABASE

    var users by mutableStateOf(emptyList<HumanPlayer>())

    /* Player 1 States */
    var player1 by mutableStateOf(placeHolderHumanPlayer)
    var player1Name by mutableStateOf("Default Player")
    var player1Timer by mutableIntStateOf(0)
    var player1Turn by mutableStateOf(true)
    var player1WinStatus by mutableStateOf(PLAYERWINSTATUS.DRAW)
    var player1Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player1Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player1StatMarker by mutableStateOf("")  // "ooo/////xx"
    var player1WinString by mutableStateOf("")    // These stat strings might need to be
    var player1DrawString by mutableStateOf("") // processed depending on backend
    var player1LossesString by mutableStateOf("")
    var player1TotalGamesString by mutableStateOf("")

    /* Player 2 States */
    var player2 by mutableStateOf(placeHolderHumanPlayer)
    var player2Name by mutableStateOf("Default Player")
    var player2Timer by mutableIntStateOf(0)
    var player2Turn by mutableStateOf(false)
    var player2WinStatus by mutableStateOf(PLAYERWINSTATUS.DRAW)
    var player2Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player2Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player2StatMarker by mutableStateOf("")  // "ooo/////xx"
    var player2WinString by mutableStateOf("")    // These stat strings might need to be
    var player2DrawString by mutableStateOf("") // processed depending on backend
    var player2LossesString by mutableStateOf("")
    var player2TotalGamesString by mutableStateOf("")

    /* Game States */
    var boardState by mutableStateOf(arrayOf<String>())
    var gameActive by mutableStateOf(true) // This and gameEnded could probably be the same
    var startingSelection by mutableIntStateOf(0) // 0 = "Player 1", 1 = "Player 2"
    var boardSelection by mutableIntStateOf(0) // 0 = 3x3, 1 = 4x4, 2 = 5x5
    var winConditionSelection by mutableIntStateOf(0) // 0 = 3, 1 = 4, 2 = 5
    var winSelectable by mutableStateOf(arrayOf(false, true, true)) // Controls button selection
    var undoAvailable by mutableStateOf(false)
    var singlePlayerGame by mutableStateOf(true)
    var winIndices by mutableStateOf(emptyArray<Boolean>()) // Fill with win when happens

    /* UI States*/
    var gameUIState by mutableStateOf(MENU.RUNNING)
    var uiSelectedPlayer by mutableStateOf(UIPLAYERSELECT.PLAYER1)
    var newUser by mutableStateOf(false)
    var selectedAvatar by mutableIntStateOf(findAvatar())
    var playerTextFieldValue by mutableStateOf("")
    var playerSwitchUI by mutableStateOf(true)
    var userSelectIndex by mutableIntStateOf(0)

    init {
        Log.v(TAG,"TicTacViewModel Created")
        Log.v("Test", "Test Log Working")

        // Makes Database
        db = Room.databaseBuilder(
            context,
            PLAYER_ROOM_DATABASE::class.java, "player-database")
            .allowMainThreadQueries()
            .build()

        gd = GameDriver(GameConfig(),db) // Makes Game driver
        generateInitialUsers() // Makes Game driver populates database if empty
        users = gd.getPlayersFromDatabase() as List<HumanPlayer> // Sets users, also makes them un-nullable
        setupDefaultProfiles()
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
            is TicTacEvent.ChangePlayer -> changePlayer(event.player)
            is TicTacEvent.MarkerPlaced -> markerPlaced(event.position)
        }
    }

    /********************************
        ** OnEvent functions **
     *******************************/
    private fun gameStart(){
        Log.v(TAG, TYPE+"StartGame")

        if(player1Turn){
            gd.setFirstPlayer(player1)
            gd.setSecondPlayer(player2)
        }else{
            gd.setFirstPlayer(player2)
            gd.setSecondPlayer(player1)
        }

        // Default values
        var boardSize = 3
        var winSize = 3

        // Get players marker
        when(player1Marker){
            0 -> {
                gd.getPlayerArray()[0]!!.playerIcon = "x"
                gd.getPlayerArray()[1]!!.playerIcon = "o"
            }
            1 -> {
                gd.getPlayerArray()[0]!!.playerIcon = "o"
                gd.getPlayerArray()[1]!!.playerIcon = "x"
            }
        }

        // sets board size and initial board state
        when(boardSelection){
            0 -> { boardSize = 3; boardState = Array(9) { _ -> "" } }
            1 -> { boardSize = 4; boardState = Array(16) { _ -> "" } }
            2 -> { boardSize = 5; boardState = Array(25) { _ -> "" } }
        }

        // Sets win condition
        when(winConditionSelection){
            0 -> winSize = 3
            1 -> winSize = 4
            2 -> winSize = 5
        }

        // Sets play order
        when(startingSelection){
            0 -> gd.setPlayerOrder(0)
            1 -> gd.setPlayerOrder(1)
        }

        gd.reinit(GameConfig(boardSize, boardSize, winSize))
    }

    private fun newSinglePlayerGame(){
        Log.v(TAG, TYPE+"NewSinglePlayerGame")

        player2 = placeHolderAIPlayer
        player2Name = "AI"

        singlePlayerGame = true
    }

    private fun newMultiPlayerGame(){
        Log.v(TAG, TYPE+"NewMultiplayerPlayerGame")

        if(player2 == users[0]) { player2Name = player2.playerName}

        singlePlayerGame = false

        gd.setFirstPlayer(player1)
        gd.setSecondPlayer(player2)
    }

    private fun profileMenuSelect(){Log.v(TAG, TYPE+"ProfileMenuSelect")}

    private fun changePlayer(player: HumanPlayer){
        when(uiSelectedPlayer){
            UIPLAYERSELECT.PLAYER1 -> {
                player1 = player
                player1Name = player.playerName
                player1Avatar = player.playerAvatar!!
                player1StatMarker = gd.getPlayerStatsRibbonFromDatabase(player)

                val stats = gd.getPlayerDisplayStatsFromDatabase(player)
                player1WinString = stats.first
                player1DrawString = stats.second
                player1LossesString = stats.third
                player1TotalGamesString = gd.getPlayerTotalGamesDisplayFromDatabase(player)
            }
            UIPLAYERSELECT.PLAYER2 -> {
                player2 = player
                player2Name = player.playerName
                player2Avatar = player.playerAvatar!!
                player2StatMarker = gd.getPlayerStatsRibbonFromDatabase(player)

                val stats = gd.getPlayerDisplayStatsFromDatabase(player)
                player2WinString = stats.first
                player2DrawString = stats.second
                player2LossesString = stats.third
                player2TotalGamesString = gd.getPlayerTotalGamesDisplayFromDatabase(player)
            }
        }
    }

    private fun undo(){Log.v(TAG, TYPE+"Undo")}
    private fun restart(){Log.v(TAG, TYPE+"Restart")}
    private fun exit(){Log.v(TAG, TYPE+"Exit")}

    private fun saveUser(){
        Log.v(TAG, TYPE+"SaveUser")
        when(uiSelectedPlayer){
            UIPLAYERSELECT.PLAYER1 -> {
                player1Name = playerTextFieldValue
                player1Avatar = avatarArray[selectedAvatar]
                gd.editPlayerDatabaseAttribute(player1,"playerName",player1Name)
                gd.editPlayerDatabaseAttribute(player1,"playerAvatar",player1Avatar)
            }
            UIPLAYERSELECT.PLAYER2 -> {
                player2Name = playerTextFieldValue
                player2Avatar = avatarArray[selectedAvatar]
                gd.editPlayerDatabaseAttribute(player2,"playerName",player2Name)
                gd.editPlayerDatabaseAttribute(player2,"playerAvatar",player2Avatar)
            }
        }
    }

    private fun timerStart(){Log.v(TAG, TYPE+"TimerStart")}
    private fun timerStop(){Log.v(TAG, TYPE+"TimerStop")}

    private fun markerPlaced(position: Int){
        val position2D = positionConverter(position)
        Log.v(TAG, TYPE+"MarkerPlaced: UIPosition = $position, GameDriverPosition = ${position2D.x},${position2D.y}")

        var wincondition = gd.playMove(position2D.x,position2D.y)

        if (wincondition == WinCondition.WIN){
            // Do stuff maybe use when
            val board2D = gd.getBoardAsString()
            boardConvertAndSet(board2D)
            print2D(board2D)
            Log.v("Test", wincondition.toString())
            return
        }else{
            swapPlayer()
        }

        if(player2 == placeHolderAIPlayer){
            wincondition = gd.playMove()
        }

        if (wincondition == WinCondition.WIN){
            // Do stuff maybe use when
            val board2D = gd.getBoardAsString()
            boardConvertAndSet(board2D)
            print2D(board2D)
            Log.v("Test", wincondition.toString())
            return
        }else{
            swapPlayer()
        }

        val board2D = gd.getBoardAsString()
        print2D(board2D)
        boardConvertAndSet(board2D)
        Log.v("Test", wincondition.toString())

        val moveUndoAvailable = boardState.find { move -> "x".equals(move) || "o".equals(move) }

        if (moveUndoAvailable != null && !singlePlayerGame){
            undoAvailable = true
        }

//        Log.v("Test", "BoardString: ${board2D.joinToString()}")

//        boardConvertAndSet(board2D)

        // Get current player
        //switch
    }

    /********************************
     ** Helper functions **
     *******************************/

    private fun swapPlayer(){
        if(player1Turn){
            player1Turn = false
            player2Turn = true
        }else{
            player1Turn = true
            player2Turn = false
        }
    }

    private fun print2D(arr:Array<Array<String>>){
        for (i in arr.indices) {
            Log.v("Test", "BoardString: ${arr[i].contentToString()}")
        }
        Log.v("Test", "")
    }
    private fun positionConverter(position1D: Int): Point {
//        val isPositive = position1D>=0
//        val absolutePosition1D = abs(position1D)
        val position2D = Point(0,0)
        position2D.x = position1D % getBoardSize()
        position2D.y = position1D / getBoardSize()

//        if (absolutePosition1D >= 0) {
//            position2D.x = absolutePosition1D % getBoardSize()
//            position2D.y = absolutePosition1D / getBoardSize()
//        } else {
//            // Swap if value came from horizontal grid layout (transposed)
//            position2D.y = absolutePosition1D % getBoardSize()
//            position2D.x = absolutePosition1D / getBoardSize()
//        }

        return position2D
    }

    private fun boardConvertAndSet(board2D: Array<Array<String>>){
        val board1D = board2D.reduce { acc, ints -> acc + ints  }

        Log.v("Test", "BoardString: ${board1D.joinToString()}")
        boardState = board1D
    }
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

    fun findAvatar(): Int{
        if(newUser){
            return 0
        }

        var currentAvatar = 0

        currentAvatar = if(uiSelectedPlayer == UIPLAYERSELECT.PLAYER1) {

            player1Avatar
        }else{
            player2Avatar
        }

        for((index, avatar) in avatarArray.withIndex()){
            if(avatar == currentAvatar){
                return index
            }
        }
        return 0
    }

    fun findUser(): Int{
        if(newUser){
            return 0
        }

        val currentUser = if(uiSelectedPlayer == UIPLAYERSELECT.PLAYER1) {
            player1
        }else{
            player2
        }

        for((index, user) in users.withIndex()){
            if(user == currentUser){
                return index
            }
        }
        return 0
    }

    fun findEditTextValue(){
        playerTextFieldValue = when(uiSelectedPlayer == UIPLAYERSELECT.PLAYER1){
            true -> {if(newUser){
                ""
            }else{
                player1Name
            }}

            false -> {if(newUser){
                ""
            }else{
                player2Name
            }}
        }
    }

    private fun generateInitialUsers(){
        if(gd.getPlayersFromDatabase().isEmpty()){
            gd.addPlayerToDatabase(HumanPlayer("Ryan",null,"O", R.drawable.avatar_8))
            gd.addPlayerToDatabase(HumanPlayer("Jasmine",null,"O", R.drawable.avatar_1))
            gd.addPlayerToDatabase(HumanPlayer("Keven",null,"O", R.drawable.avatar_6))
            gd.addPlayerToDatabase(HumanPlayer("Sajib",null,"O", R.drawable.avatar_3))
            gd.addPlayerToDatabase(HumanPlayer("Wendy",null,"O", R.drawable.avatar_5))
            gd.addPlayerToDatabase(HumanPlayer("Debbie",null,"O", R.drawable.avatar_9))
            gd.addPlayerToDatabase(HumanPlayer("Stuart",null,"O", R.drawable.avatar_2))
            gd.addPlayerToDatabase(HumanPlayer("Jax",null,"O", R.drawable.avatar_4))
            gd.addPlayerToDatabase(HumanPlayer("Sally",null,"O", R.drawable.avatar_7))
            gd.addPlayerToDatabase(HumanPlayer("Kathy",null,"O", R.drawable.avatar_10))

            for (user: HumanPlayer? in gd.getPlayersFromDatabase()){
                if (user != null) {
                    gd.updatePlayerStatsInDatabase(user,Random.nextInt(0, 10),Random.nextInt(0, 10),Random.nextInt(0, 10))
                }
            }
            // Sets up a default player with ID 0 and 0 stats and random avatar
            gd.addPlayerToDatabase(HumanPlayer("Default Player",0,"O", avatarArray[Random.nextInt(0, 10)]))
            gd.getPlayerFromDatabase(0)
                ?.let { gd.updatePlayerStatsInDatabase(it,0,0,0) }
        }
    }

    private fun setupDefaultProfiles(){
        player1 = users[0]
        player1Name = users[0].playerName
        player1Avatar = users[0].playerAvatar!!

        player2 = users[0]
        player2Name = users[0].playerName
        player2Avatar = users[0].playerAvatar!!
    }
}