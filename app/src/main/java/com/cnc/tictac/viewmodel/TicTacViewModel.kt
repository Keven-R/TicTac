package com.cnc.tictac.viewmodel

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.cnc.tictac.R
import com.cnc.tictac.backend.database.PLAYER_ROOM_DATABASE
import com.cnc.tictac.backend.gamedriver.GameConfig
import com.cnc.tictac.backend.gamedriver.GameDriver
import com.cnc.tictac.backend.system.AIPlayer
import com.cnc.tictac.backend.system.HumanPlayer
import com.cnc.tictac.backend.system.Player
import com.cnc.tictac.backend.system.WinCondition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val TAG = "TicTacViewModel"
private const val SMTAG = "StateMachine"
private const val TYPE = "EVENT: "

enum class PLAYERWINSTATUS { NONE, LOSS, DRAW, WIN }
enum class UIPLAYERSELECT { PLAYER1, PLAYER2 }
enum class MENU { RUNNING, PAUSE, RESTART, EXIT, UNDO }

class TicTacViewModel(context: Context) : ViewModel(){
    /* For establishing some initial UI states */
    val avatarArray = arrayOf(R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
        R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7,
        R.drawable.avatar_8, R.drawable.avatar_9, R.drawable.avatar_10)

    /* Backend Variables */
    private var placeHolderPlayer: Player = HumanPlayer()
    private var placeHolderAIPlayer: Player = AIPlayer()
    var gd : GameDriver
    var db : PLAYER_ROOM_DATABASE

    var users by mutableStateOf(emptyList<HumanPlayer>())
    var leaderboard by mutableStateOf(emptyList<HumanPlayer>())

    /** Current view model state machine state- starts OUTSIDE_GAME **/
    var CurrentState : STATE? = STATE.OUTSIDE_GAME

    /* Player 1 States */
    var player1 by mutableStateOf(placeHolderPlayer)
    var player1Name by mutableStateOf("Default Player")
    var player1Timer by mutableIntStateOf(10)
    var player1Turn by mutableStateOf(true)
    var player1Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player1Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player1StatMarker by mutableStateOf("")  // "ooo/////xx"
    var player1WinStatus by mutableStateOf(PLAYERWINSTATUS.NONE)
    var player1WinString by mutableStateOf("")    // These stat strings might need to be
    var player1DrawString by mutableStateOf("") // processed depending on backend
    var player1LossesString by mutableStateOf("")
    var player1TotalGamesString by mutableStateOf("")

    /* Player 2 States */
    var player2 by mutableStateOf(placeHolderPlayer)
    var player2Name by mutableStateOf("Default Player")
    var player2Timer by mutableIntStateOf(10)
    var player2Turn by mutableStateOf(false)
    var player2Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player2Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player2StatMarker by mutableStateOf("")  // "ooo/////xx"
    var player2WinStatus by mutableStateOf(PLAYERWINSTATUS.NONE)
    var player2WinString by mutableStateOf("")    // These stat strings might need to be
    var player2DrawString by mutableStateOf("") // processed depending on backend
    var player2LossesString by mutableStateOf("")
    var player2TotalGamesString by mutableStateOf("")

    /* Game States */
    var boardState by mutableStateOf(arrayOf<String>())
    var gameActive by mutableStateOf(true) // This and gameEnded could probably be the same
    var timerActive by mutableStateOf(false) // This and gameEnded could probably be the same
    var startingSelection by mutableIntStateOf(0) // 0 = "Player 1", 1 = "Player 2"
    var boardSelection by mutableIntStateOf(0) // 0 = 3x3, 1 = 4x4, 2 = 5x5
    var winConditionSelection by mutableIntStateOf(0) // 0 = 3, 1 = 4, 2 = 5
    var winSelectable by mutableStateOf(arrayOf(false, true, true)) // Controls button selection
    var undoAvailable by mutableStateOf(false)
    var singlePlayerGame by mutableStateOf(true)
    var winIndices by mutableStateOf(emptyArray<Boolean>()) // Fill with win when happens
    var winner by mutableStateOf(HumanPlayer() as Player?)
    var winCondition by mutableStateOf(WinCondition.NO_WIN as WinCondition?)

    /* UI States*/
    var gameUIState by mutableStateOf(MENU.RUNNING)
    var uiSelectedPlayer by mutableStateOf(UIPLAYERSELECT.PLAYER1)
    var newUser by mutableStateOf(false)
    var selectedAvatar by mutableIntStateOf(findAvatar())
    var playerTextFieldValue by mutableStateOf("")
    var playerSwitchUI by mutableStateOf(true)
    var userSelectIndex by mutableIntStateOf(0)

    init {
        Log.v(TAG, "TicTacViewModel Created")
        Log.v("Test", "Test Log Working")
        // Makes Database
        db = Room.databaseBuilder(
            context,
            PLAYER_ROOM_DATABASE::class.java, "player-database"
        )
            .allowMainThreadQueries()
            .build()
        // Create game driver
        gd = GameDriver(GameConfig(), db) // Makes Game driver

        generateInitialUsers() // Makes Game driver populates database if empty
        // Sets users, also makes them un-nullable
        users = gd.getPlayersFromDatabase() as List<HumanPlayer>
        // Fetch the leaderboard data and populate mutable state
        leaderboard = gd.getLeaderboard() as List<HumanPlayer>
        // winner is set to null in advance to avoid errors
        winner = null
        // Configure default profiles in database
        setupDefaultProfiles()
        ticker()
        // Change state to game start
    }
    // is keyword for when its a dataclass and takes parameters (can be on all of them but helps separate them)
    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.TempEvent -> Log.v(TAG, TYPE+"Temp Event For Testing")
            /** Creating a new single player or multiplayer game starts the state machine
             * outside the loop at OUTSIDE_GAME **/
            TicTacEvent.NewSinglePlayerGame -> {
                this.CurrentState = this.CurrentState.ChangeState(STATE.OUTSIDE_GAME)
                newSinglePlayerGame()
            }
            TicTacEvent.NewMultiPlayerGame  -> {
                this.CurrentState = this.CurrentState.ChangeState(STATE.OUTSIDE_GAME)
                newMultiPlayerGame()
            }
            /** event StartGame enters the loop either at player1's move or player2's move **/
            TicTacEvent.StartGame -> {
                gameStart()
                this.CurrentState = if(player1Turn) {
                    this.CurrentState.ChangeState(STATE.PLAYER1_TURN)
                }else{
                    this.CurrentState.ChangeState((STATE.PLAYER2_TURN))
                }
            }
            /** Undo switches players around **/
            TicTacEvent.Undo -> {
                undo()
                updateNewMarker()
                swapPlayer()
                this.CurrentState = if(player1Turn) {
                    this.CurrentState.ChangeState(STATE.PLAYER2_TURN)
                }else{
                    this.CurrentState.ChangeState((STATE.PLAYER1_TURN))
                }
            }
            /** Restart switches players around **/
            TicTacEvent.Restart -> {
                restart()
                updateNewMarker()
                if(!singlePlayerGame) {
                    swapPlayer()
                    this.CurrentState = if (player1Turn) {
                        this.CurrentState.ChangeState(STATE.PLAYER2_TURN)
                    } else {
                        this.CurrentState.ChangeState((STATE.PLAYER1_TURN))
                    }
                } else {
                    if(gd.whoIsPlaying() is HumanPlayer){
                        swapPlayer()
                    }
                    this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER2_TURN)
                }
            }
            /** Exit changes game start to GAME_OVER **/
            TicTacEvent.Exit -> {
                this.CurrentState.ChangeState(STATE.GAME_OVER)
                exit()
            }
            /** Marker placed changes players around after method completes **/
            is TicTacEvent.MarkerPlaced -> {
                markerPlaced(event.position)
                updateNewMarker()
                searchingForWin()
                populateWinIndices()
                swapPlayer()
            }
            is TicTacEvent.ChangePlayer -> changePlayer(event.player)
            TicTacEvent.ProfileMenuSelect -> profileMenuSelect()
            TicTacEvent.SaveUser -> saveUser()
            TicTacEvent.TimerStart -> timerStart()
            TicTacEvent.TimerStop -> timerStop()
        }
    }
    private fun gameStart(){
        /** To start the game, the state machine must first be in the state OUTSIDE_GAME **/
        if (CurrentState == STATE.OUTSIDE_GAME) {
            Log.d(SMTAG, "State machine is GAME_START: gameStart")
            // Configure who starts first in the backend
            if (player1Turn) {
                gd.setFirstPlayer(player1)
                gd.setSecondPlayer(player2)
            } else {
                gd.setFirstPlayer(player2)
                gd.setSecondPlayer(player1)
            }
            // Default values
            var boardSize = 3
            var winSize = 3
            // Get players marker
            when (player1Marker) {
                0 -> {  gd.getPlayerArray()[0]!!.playerIcon = "x"
                        gd.getPlayerArray()[1]!!.playerIcon = "o" }
                1 -> {  gd.getPlayerArray()[0]!!.playerIcon = "o"
                        gd.getPlayerArray()[1]!!.playerIcon = "x" }
            }
            // sets board size and initial board state
            when (boardSelection) {
                0 -> { boardSize = 3; boardState = Array(9) { _ -> "" } }
                1 -> { boardSize = 4; boardState = Array(16) { _ -> "" } }
                2 -> { boardSize = 5; boardState = Array(25) { _ -> "" } }
            }
            // Sets win condition
            when (winConditionSelection) {
                0 -> winSize = 3
                1 -> winSize = 4
                2 -> winSize = 5
            }
            // Sets play order
            when (startingSelection) {
                0 -> {
                    gd.setPlayerOrder(0)
                    this.player1Turn = true
                    this.player2Turn = false
                }
                1 -> {
                    gd.setPlayerOrder(1)
                    this.player1Turn = false
                    this.player2Turn = true
                }
            }
            // Regenerates game to new configuration
            gd.reinit(GameConfig(boardSize, boardSize, winSize))
            // Resets mutable states
            this.resetMutableStates()
            timerStart()
        }
    }
    private fun newSinglePlayerGame(){
        if (CurrentState == STATE.OUTSIDE_GAME) {
            Log.d(SMTAG, "State machine is GAME_START: newSinglePlayerGame")
            Log.v(TAG, TYPE + "NewSinglePlayerGame")
            player2 = placeHolderAIPlayer
            player2Name = "AI"
            singlePlayerGame = true
        }
    }
    private fun newMultiPlayerGame(){
        if (CurrentState == STATE.OUTSIDE_GAME) {
            Log.d(SMTAG, "State machine is GAME_START: newMultiPlayerGame")
            Log.v(TAG, TYPE + "NewMultiplayerPlayerGame")
            if (player2 == users[0]) {
                player2Name = player2.playerName
            }
            singlePlayerGame = false
            gd.setFirstPlayer(player1)
            gd.setSecondPlayer(player2)
        }
    }
    private fun markerPlaced(position: Int){
        val position2D = positionConverter(position)
        when(this.CurrentState){
            STATE.PLAYER1_TURN -> {
                this.winCondition = gd.playMove(position2D.x,position2D.y)
                this.CurrentState = this.CurrentState.ChangeState(STATE.SEARCHING_P1_WIN)
                return
            }
            STATE.PLAYER2_TURN -> {
                when(this.player2) {
                    is AIPlayer -> this.winCondition = gd.playMove()
                    is HumanPlayer -> this.winCondition = gd.playMove(position2D.x, position2D.y)
                    else -> Log.e(TAG, "Second player is not AIPlayer or HumanPlayer")
                }
                this.CurrentState = this.CurrentState.ChangeState(STATE.SEARCHING_P2_WIN)
                return
            }
            STATE.GAME_OVER -> {
                return
            }
            else -> {
                Log.e(SMTAG, "STATE when placing puck was not PLAYER1_MOVE or PLAYER2_MOVE.")
            }
        }
    }
    /** Called whenever a winner is detected after a marker is placed
     * > Updates this.winner
     * > Updates this.winCondition
     * > Updates this.winIndices
     */
    private fun populateWinIndices() {
        val constraints = gd.getBoard().getConstraints()
        when(this.winCondition){
            WinCondition.NO_WIN -> this.winIndices = Array(constraints.first*constraints.second, { false })
            WinCondition.DRAW -> this.winIndices = Array(constraints.first*constraints.second, { true })
            WinCondition.VERTICAL, WinCondition.HORISONTAL -> {
                val winCoordinates = gd.getWinCoordinates(this.winCondition!!)!!
                for(i in winCoordinates.first.first .. winCoordinates.second.first) {
                    for (j in winCoordinates.first.second..winCoordinates.second.second) {
                        this.winIndices[positionConverter1D(Point(i, j))] = true
                    }
                }
            }
            WinCondition.DIAGONAL_1 -> {
                val winCoordinates = gd.getWinCoordinates(this.winCondition!!)!!
                for(i in winCoordinates.first.first .. winCoordinates.second.first) {
                    for (j in winCoordinates.first.second..winCoordinates.second.second) {
                        if(i == j){
                            this.winIndices[positionConverter1D(Point(i, j))] = true
                        }
                    }
                }
            }
            WinCondition.DIAGONAL_2 -> {
                val winCoordinates = gd.getWinCoordinates(this.winCondition!!)
                for(i in winCoordinates!!.first.first .. winCoordinates.second.first) {
                    for (j in winCoordinates!!.second.second..winCoordinates.first.second) {
                        if(i == -j + gd.getMinimumWin() - 1){
                            this.winIndices[positionConverter1D(Point(i, j))] = true
                        }
                    }
                }
            }
            null -> {
                Array(constraints.first*constraints.second, { false })
            }
        }
    }
    private fun searchingForWin() {
        val constraints = gd.getBoard().getConstraints()
        this.winIndices = Array(constraints.first*constraints.second, { false })
        when(this.CurrentState) {
            STATE.SEARCHING_P1_WIN -> {
                when (this.winCondition) {
                    WinCondition.NO_WIN -> {
                        this.winner = null
                        this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER2_TURN)
                        return
                    }
                    WinCondition.DRAW -> {
                        this.winner = null
                        this.player1WinStatus = PLAYERWINSTATUS.DRAW
                        this.player2WinStatus = PLAYERWINSTATUS.DRAW
                        this.gameActive = false
                        this.CurrentState = this.CurrentState.ChangeState(STATE.GAME_OVER)
                        return
                    }
                    // When a puck is placed on an occupied square
                    null -> {
                        this.winner = null
                        this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER1_TURN)
                        return
                    }
                    // When a win occurs
                    is WinCondition -> {
                        this.winner = player1
                        this.player1WinStatus = PLAYERWINSTATUS.WIN
                        this.player2WinStatus = PLAYERWINSTATUS.LOSS
                        this.gameActive = false
                        this.CurrentState = this.CurrentState.ChangeState(STATE.GAME_OVER)
                    }
                }
            }
            STATE.SEARCHING_P2_WIN -> {
                when (this.winCondition) {
                    WinCondition.NO_WIN -> {
                        this.winner = null
                        this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER1_TURN)
                        return
                    }
                    WinCondition.DRAW -> {
                        this.winner = null
                        this.player1WinStatus = PLAYERWINSTATUS.DRAW
                        this.player2WinStatus = PLAYERWINSTATUS.DRAW
                        this.gameActive = false
                        this.CurrentState = this.CurrentState.ChangeState(STATE.GAME_OVER)
                        return
                    }
                    // When a puck is placed on an occupied square
                    null -> {
                        this.winner = null
                        this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER2_TURN)
                        return
                    }
                    // When a win occurs
                    is WinCondition -> {
                        this.winner = player2
                        this.player2WinStatus = PLAYERWINSTATUS.WIN
                        this.player1WinStatus = PLAYERWINSTATUS.LOSS
                        this.gameActive = false
                        this.CurrentState = this.CurrentState.ChangeState(STATE.GAME_OVER)
                    }
                }
            }
            else -> {
                this.winner = null
                this.CurrentState = this.CurrentState.ChangeState(STATE.GAME_OVER)
            }
        }
    }

    private fun profileMenuSelect(){Log.v(TAG, TYPE+"ProfileMenuSelect")}

    private fun resetMutableStates(){
        this.winner = null
        this.gameActive = true
        this.winCondition = WinCondition.NO_WIN
        val constraints = gd.getBoard().getConstraints()
        this.winIndices = Array(constraints.first*constraints.second, { false })
    }
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

    private fun undo(){
        if (!singlePlayerGame) {
            gd.undoPreviousMove()
        }
        this.resetMutableStates()
        val board2D = gd.getBoardAsString()
        boardConvertAndSet(board2D)
    }
    private fun restart(){
        Log.v(TAG, TYPE + "Restart")
        gd.resetGameBoard()
        boardConvertAndSet(gd.getBoardAsString()) // redraws board
        this.resetMutableStates()
    }

    private fun exit(){
        Log.v(TAG, TYPE + "Exit")
        // When someone WON or a DRAW occured
        when(this.winCondition) {
            WinCondition.DRAW -> {
                if(!this.singlePlayerGame) {
                    val currentWinnerStats =
                        gd.getPlayerStatsFromDatabase(this.player2 as HumanPlayer)
                    val p2wins = currentWinnerStats.first
                    val p2losses = currentWinnerStats.second
                    var p2draws = currentWinnerStats.third
                    gd.updatePlayerStatsInDatabase(this.player2 as HumanPlayer, p2wins, ++p2draws, p2losses)
                }
                val currentWinnerStats = gd.getPlayerStatsFromDatabase(this.player1 as HumanPlayer)
                val p1wins = currentWinnerStats.first
                val p1losses = currentWinnerStats.second
                var p1draws = currentWinnerStats.third
                gd.updatePlayerStatsInDatabase(this.player1 as HumanPlayer, p1wins, ++p1draws, p1losses)
                // exit
                this.resetMutableStates()
                gd.resetGameBoard()
                users = gd.getPlayersFromDatabase() as List<HumanPlayer> // reloads the user list for updated info
                return
            }
            WinCondition.NO_WIN, null -> {
                // exit
                this.resetMutableStates()
                gd.resetGameBoard()
                users = gd.getPlayersFromDatabase() as List<HumanPlayer> // reloads the user list for updated info
                return
            }
            // Someone Won
            else -> {
                if (singlePlayerGame && this.winner != null && this.winner is HumanPlayer) {
                    val currentWinnerStats = gd.getPlayerStatsFromDatabase(this.winner as HumanPlayer)
                    var p1wins = currentWinnerStats.first
                    val p1losses = currentWinnerStats.second
                    val p1draws = currentWinnerStats.third
                    gd.updatePlayerStatsInDatabase(this.winner as HumanPlayer, ++p1wins, p1draws, p1losses)
                } else if (this.winner != null && this.winner == this.player1) {
                    val player1stats = gd.getPlayerStatsFromDatabase(this.player1 as HumanPlayer)
                    var p1wins = player1stats.first
                    val p1losses = player1stats.second
                    val p1draws = player1stats.third
                    gd.updatePlayerStatsInDatabase(this.player1 as HumanPlayer, ++p1wins, p1draws, p1losses)
                    val player2stats = gd.getPlayerStatsFromDatabase(this.player2 as HumanPlayer)
                    val p2wins = player2stats.first
                    var p2losses = player2stats.second
                    val p2draws = player2stats.third
                    gd.updatePlayerStatsInDatabase(this.player2 as HumanPlayer, p2wins, p2draws, ++p2losses)
                } else if (this.winner != null && this.winner == this.player2) {
                    val player1stats = gd.getPlayerStatsFromDatabase(this.player1 as HumanPlayer)
                    val p1wins = player1stats.first
                    var p1losses = player1stats.second
                    val p1draws = player1stats.third
                    gd.updatePlayerStatsInDatabase(this.player1 as HumanPlayer, p1wins, p1draws, ++p1losses)
                    val player2stats = gd.getPlayerStatsFromDatabase(this.player2 as HumanPlayer)
                    var p2wins = player2stats.first
                    val p2losses = player2stats.second
                    val p2draws = player2stats.third
                    gd.updatePlayerStatsInDatabase(this.player2 as HumanPlayer, ++p2wins, p2draws, p2losses)
                }
                this.resetMutableStates()
                gd.resetGameBoard()
                users = gd.getPlayersFromDatabase() as List<HumanPlayer> // reloads the user list for updated info
                return
            }
        }
    }

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

    private fun timerStart(){
        // Probably doesn't need to be an event but the log helps
        Log.v(TAG, TYPE+"TimerStart")
        timerActive = true
    }

    private fun timerStop(){
        // Probably doesn't need to be an event but the log helps
        Log.v(TAG, TYPE+"TimerStop")
        timerActive = false
    }





    /** updateNewMarker
     * > updates board from backend to frontend
     * > resets timer for player 1 and player 2
     */
    private fun updateNewMarker(){
        /** Print board to debugging output **/
        val board2D = gd.getBoardAsString()
        print2D(board2D)
        boardConvertAndSet(board2D)
        val moveUndoAvailable = boardState.find { move -> "x".equals(move) || "o".equals(move) }
        undoAvailable = moveUndoAvailable != null && !singlePlayerGame
        if(singlePlayerGame) {
            player1Timer = 10
            player2Timer = 0
        } else {
            player1Timer = 10
            player2Timer = 10
        }
    }

    /** markerPlacedAI
     * > Places marker for AI player.
     * > Called from markerPlaced if in single player
     */

    /********************************
     ** Helper functions **
     *******************************/

    private fun swapPlayer(){
        if(this.CurrentState != STATE.GAME_OVER) {
            if (player1Turn) {
                player1Turn = false
                player2Turn = true
                Log.v("Test", "Player 2 Turn")
            } else {
                player1Turn = true
                player2Turn = false
                Log.v("Test", "Player 1 Turn")
            }
            this.gd.nextPlayer()
            Log.d(TAG, "Current player is ${this.gd.whoIsPlaying()!!.playerName}")
        }
    }

    private fun print2D(arr:Array<Array<String>>){
        for (i in arr.indices) {
            Log.v("Test", "BoardString: ${arr[i].contentToString()}")
        }
        Log.v("Test", "")
    }

    private fun print1D(arr:Array<Any>){
        for (i in arr.indices) {
            Log.v("Test", "${arr[i]}")
        }
        Log.v("Test", "")
    }
    private fun positionConverter(position1D: Int): Point {
        val position2D = Point(0,0)
        position2D.x = position1D % getBoardSize()
        position2D.y = position1D / getBoardSize()
        return position2D
    }
    private fun positionConverter1D(position2D: Point): Int {
        var position1D : Int = position2D.x + (position2D.y * gd.getBoard().getConstraints().first)
        return position1D
    }
    private fun boardConvertAndSet(board2D: Array<Array<String>>){
        val board1D = board2D.reduce { acc, ints -> acc + ints  }
        Log.v("Test", "BoardString: ${board1D.joinToString()}")
        boardState = board1D
    }
    fun getMarkerSymbol(player: Int): String{
        if(this.player1Marker == 0 && player == 1){
            return "X"
        } else if(this.player1Marker == 1 && player == 1){
            return "O"
        } else if(this.player1Marker == 0 && player == 2){
            return "O"
        } else if(this.player1Marker == 1 && player == 2){
            return "X"
        } else {
            return "X"
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

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private fun ticker(){
        // This will tick the players counter down by 1 second
        tickerFlow(1.seconds)
            .map { -1 }
            .onEach {
                if(timerActive){
                    if(player1Turn){
                        player1Timer += it
                    } else{
                        player2Timer += it
                    }
                }
                if(timerActive && singlePlayerGame && player2Turn){
                    this.onEvent(TicTacEvent.MarkerPlaced(0))
                } else if((timerActive && player1Turn && player1Timer == 0)
                    || (timerActive && player2Turn && player2Timer == 0)){
                    swapPlayer()
                    if(this.CurrentState == STATE.PLAYER1_TURN){
                        this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER2_TURN)
                    } else if(this.CurrentState == STATE.PLAYER2_TURN){
                        this.CurrentState = this.CurrentState.ChangeState(STATE.PLAYER1_TURN)
                    }
                }
            }.launchIn(viewModelScope)
    }

    // Returns stats as a triple, already in string format. <Wins, Draws, Loss>
    fun getPlayerStats (player : HumanPlayer) : Triple<Int, Int, Int> {
        val stats = gd.getPlayerStatsFromDatabase(player)
        val numWin = stats.first
        val numDraw = stats.second
        val numLoss = stats.third

        return Triple(numWin, numDraw, numLoss)
    }

    // Returns total number of games in string format.
    fun getPlayerTotalGames (player : HumanPlayer) : Int {
        return gd.getPlayerTotalGamesFromDatabase(player)
    }
}