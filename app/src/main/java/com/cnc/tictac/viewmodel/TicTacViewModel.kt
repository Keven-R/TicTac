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
private const val TYPE = "EVENT: "

enum class PLAYERWINSTATUS { LOSS, DRAW, WIN , NULL}
enum class UIPLAYERSELECT { PLAYER1, PLAYER2 }
enum class MENU { HIDDEN, PAUSE, RESTART, EXIT, UNDO }

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

    /* Player 1 States */
    var player1 by mutableStateOf(placeHolderPlayer)
    var player1Name by mutableStateOf("Default Player")
    var player1Timer by mutableIntStateOf(10)
    var player1Turn by mutableStateOf(true)
    var player1WinStatus by mutableStateOf(PLAYERWINSTATUS.NULL)
    var player1Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player1Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player1StatMarker by mutableStateOf("")  // "ooo/////xx"
    var player1WinString by mutableStateOf("")    // These stat strings might need to be
    var player1DrawString by mutableStateOf("") // processed depending on backend
    var player1LossesString by mutableStateOf("")
    var player1TotalGamesString by mutableStateOf("")

    /* Player 2 States */
    var player2 by mutableStateOf(placeHolderPlayer)
    var player2Name by mutableStateOf("Default Player")
    var player2Timer by mutableIntStateOf(10)
    var player2Turn by mutableStateOf(false)
    var player2WinStatus by mutableStateOf(PLAYERWINSTATUS.NULL)
    var player2Avatar by mutableIntStateOf(R.drawable.avatar_1)
    var player2Marker by mutableIntStateOf(0) // 0 = 'X', 1 = 'O'
    var player2StatMarker by mutableStateOf("")  // "ooo/////xx"
    var player2WinString by mutableStateOf("")    // These stat strings might need to be
    var player2DrawString by mutableStateOf("") // processed depending on backend
    var player2LossesString by mutableStateOf("")
    var player2TotalGamesString by mutableStateOf("")

    /* Game settings states */
    // Radio variables in GameSettingsScreens
    val markerOptions = arrayOf("x", "o")
    var startOptions by mutableStateOf(arrayOf(player1Name, player2Name))
    val boardOptions = arrayOf("3x3", "4x4", "5x5")
    val winOptions = arrayOf("3", "4", "5")

    // Radio onclick events
    val updateP1Marker =  {
        when(player1Marker) {
            0 -> {
                player1Marker = 1
            }
            1 -> {
                player1Marker = 0
            }
        }
    }
    val updateWhoGoesFirst: () -> Unit = {
        when(startingSelection){
            0 -> startingSelection = 1
            1 -> startingSelection = 0
        }
    }
    val updateBoardSize: () -> Unit = {
        when(boardSelection){
            0 -> {
                boardSelection = 1
                winSelectable = arrayOf(false, false, true)
            }
            1 -> {
                boardSelection = 2
                winSelectable = arrayOf(false, false, false)
            }
            2 -> {
                boardSelection = 0
                winSelectable = arrayOf(false, true, true)
            }
        }
    }
    val updateWinCondition: () -> Unit = {
        when(winConditionSelection){
            0 -> {
                if(winSelectable[1]){
                    winConditionSelection = 0
                }
                else{
                    winConditionSelection = 1
                }
            }
            1 -> {
                if(winSelectable[2]){
                    winConditionSelection = 0
                }
                else{
                    winConditionSelection = 2
                }
            }
            2 -> {
                winConditionSelection = 0
            }
        }
    }

    /* Game States */
    var boardState by mutableStateOf(arrayOf<String>())
    var gameActive by mutableStateOf(true) // This and gameEnded could probably be the same
    var timerActive by mutableStateOf(false) // This and gameEnded could probably be the same
    var startingSelection by mutableIntStateOf(0) // 0 = "Player 1", 1 = "Player 2"
    var boardSelection by mutableIntStateOf(0) // 0 = 3x3, 1 = 4x4, 2 = 5x5
    var winConditionSelection by mutableIntStateOf(0) // 0 = 3, 1 = 4, 2 = 5
    var winSelectable by mutableStateOf(arrayOf(false, true, true)) // Controls button selection
    var movesMade by mutableIntStateOf(0)
    var undoAvailable by mutableStateOf(false)
    var restartAvailable by mutableStateOf(true)
    var pauseAvailable by mutableStateOf(true)
    var singlePlayerGame by mutableStateOf(true)
    var winIndices by mutableStateOf(emptyArray<Boolean>()) // Fill with win when happens
    var winner by mutableStateOf(HumanPlayer() as Player?)
    var winCondition by mutableStateOf(WinCondition.NO_WIN as WinCondition?)

    /* UI States*/
    var gameUIState by mutableStateOf(MENU.HIDDEN)
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
        leaderboard = gd.getLeaderboard() as List<HumanPlayer>
        winner = null
        leaderboard = gd.getLeaderboard() as List<HumanPlayer>
        setupDefaultProfiles()
        ticker()
    }

    // is keyword for when its a dataclass and takes parameters (can be on all of them but helps separate them)
    fun onEvent(event: TicTacEvent){
        when(event){
            TicTacEvent.ProfileMenuSelect -> {} // Redundant but too long to remove
            TicTacEvent.TempEvent -> Log.v(TAG, TYPE+"Temp Event For Testing")
            TicTacEvent.StartGame -> gameStart()
            TicTacEvent.NewSinglePlayerGame -> newSinglePlayerGame()
            TicTacEvent.NewMultiPlayerGame  -> newMultiPlayerGame()
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
        /* Starting a new game should set the following:
         * 0 Confirm: board size & win condition, players active in game
         * 1 SET - all relevant game states
         * 2 - Create players
         * 3 - Set all player states (if not already)
         * 4 - Set correct board size and wincondition
         * 5 - Set player order
         * 6 - Undo shouldn't be available
         */
        Log.v(TAG, TYPE+"StartGame")
        Log.v(TAG, TYPE+"P1 = $player1Name, P2 = $player2Name")
        // 0 - Set correct board size (already updated in GameSettingsScreen
        Log.v(TAG, TYPE+"Board size = ${getBoardSize()}, Win = $winConditionSelection")

        // 1 - SET the following game states
        gameActive = true
        timerActive = true
        player1Timer = 10
        player2Timer = 10
        undoAvailable = false
        restartAvailable = true
        pauseAvailable = true
        winner = null
        winCondition = WinCondition.NO_WIN
        gameUIState = MENU.HIDDEN
        movesMade = 0
        winner = null
        winCondition = null
        player1WinStatus = PLAYERWINSTATUS.NULL
        player2WinStatus = PLAYERWINSTATUS.NULL

        // 2 - Create players
        if(player1Turn){
            gd.setFirstPlayer(player1)
            gd.setSecondPlayer(player2)
        }else{
            gd.setFirstPlayer(player2)
            gd.setSecondPlayer(player1)
        }

        // 3 - Set all player states (if not already)
        when(player1Marker){
            0 -> {
                gd.getPlayerArray()[0]!!.playerIcon = "x"
                gd.getPlayerArray()[1]!!.playerIcon = "o"
                player2Marker = 1
            }
            1 -> {
                gd.getPlayerArray()[0]!!.playerIcon = "o"
                gd.getPlayerArray()[1]!!.playerIcon = "x"
                player2Marker = 0
            }
        }

        // 4 - Set correct board size and wincondition
        val boardSize = when(boardSelection){
            0 -> { 3 }
            1 -> { 4 }
            else -> { 5 }
        }

        boardState = Array(boardSize*boardSize) { _ -> "" }
        val winSize = when(winConditionSelection){
            0 -> 3
            1 -> 4
            else -> 5
        }
        gd.reinit(GameConfig(boardSize, boardSize, winSize))

        // 5 - Set player order
        when(startingSelection){
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

        // 6 - Undo shouldn't be available
        undoAvailable = false

        // Game ready to start
        resetMutableStates()
        timerStart()

        if(player2 is AIPlayer) {
            if(player2Turn) {
                markerPlacedAI()
            }
        }
    }

    // Ends the game and sets the correct states for UI to display in GameScreen
    // Do not update anything that will cause players to not be able to restart
    private fun GameEnd() {
        // 1 - SET the following game states
        gameActive = false
        timerActive = false

        movesMade = 0
        undoAvailable = false
        restartAvailable = false
        pauseAvailable = false

        gameUIState = MENU.HIDDEN
        // DO NOT RESET win status here.
    }

    // Resets selections in game settings screen
    internal fun resetSettings() {
        startingSelection = 0
        boardSelection = 0
        winConditionSelection = 0
        winSelectable = arrayOf(false, true, true)
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

    private fun resetMutableStates(){
        this.winner = null
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
        Log.v(TAG, TYPE+"Undo")
        /** If single player, undo 2 moves **/
        if(singlePlayerGame){
            gd.undoPreviousMove()
            gd.undoPreviousMove()
        } else if(!singlePlayerGame){
            gd.undoPreviousMove()
            swapPlayer()
        }
        this.resetMutableStates()
        val board2D = gd.getBoardAsString()
        boardConvertAndSet(board2D)
        movesMade -= 2
        timerStart()
    }
    private fun restart(){
        Log.v(TAG, TYPE+"Restart")
        gd.resetGameBoard()
//        swapPlayer() //swaps player so the person who resets is always second
//        boardConvertAndSet(gd.getBoardAsString()) // redraws board
        movesMade = 0
        gameStart()
//        this.resetMutableStates()
    }

    private fun exit(){
        Log.v(TAG, TYPE+"Exit")
        if(this.winCondition != WinCondition.NO_WIN && this.winner is HumanPlayer){
            val currentWinnerStats = gd.getPlayerStatsFromDatabase(this.winner as HumanPlayer)
            var wins = currentWinnerStats.second
            var losses = currentWinnerStats.first
            var draws = currentWinnerStats.third
            when(this.winCondition){
                WinCondition.DRAW -> draws ++ // update draws
                WinCondition.VERTICAL, WinCondition.HORISONTAL,
                WinCondition.DIAGONAL_2, WinCondition.DIAGONAL_1 -> wins ++
                else -> Log.v(TAG, "Not updating stats. no win condition achieved.")
            }
            gd.updatePlayerStatsInDatabase(this.winner as HumanPlayer,wins,draws,losses) // TODO: Play stats don't seem to update
        }
        this.resetMutableStates()
        gd.resetGameBoard()
        users = gd.getPlayersFromDatabase() as List<HumanPlayer> // reloads the user list for updated info
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

    /** Called whenever a winner is detected after a marker is placed
     * > Updates this.winner
     * > Updates this.winCondition
     * > Updates this.winIndices
     */
    private fun winnerDecided(winner : Player?, winCondition : WinCondition?, winCoordinates : Pair<Pair<Int, Int>, Pair<Int, Int>>?) {
        Log.d(TAG, "Winner decided")
        val constraints = gd.getBoard().getConstraints()
        this.winIndices = Array(constraints.first*constraints.second, { false })
        when(winner){
            null -> { /** DRAW **/
                Log.d(TAG, "Game ended in a draw.")
                this.winner = null
            }
            is HumanPlayer, is AIPlayer -> { /** PLAYER WON **/
                Log.d(TAG, "${winner.playerName} won the game.")
                this.winCondition = winCondition
                this.winner = gd.whoIsPlaying()
                when(winCondition){
                    WinCondition.VERTICAL, WinCondition.HORISONTAL -> {
                        Log.d("Test", "${winCoordinates!!.first.first}:${winCoordinates.second.first}")
                        Log.d("Test", "${winCoordinates.first.second}:${winCoordinates.second.second}")
                        for(i in winCoordinates.first.first .. winCoordinates.second.first) {
                            for (j in winCoordinates.first.second..winCoordinates.second.second) {
                                this.winIndices[positionConverter1D(Point(i, j))] = true
                            }
                        }
                    }
                    WinCondition.DIAGONAL_1 -> {
                        Log.d("Test", "${winCoordinates!!.first.first}:${winCoordinates.second.first}")
                        Log.d("Test", "${winCoordinates.first.second}:${winCoordinates.second.second}")
                        for(i in winCoordinates.first.first .. winCoordinates.second.first) {
                            for (j in winCoordinates.first.second..winCoordinates.second.second) {
                                if(i == j){
                                    this.winIndices[positionConverter1D(Point(i, j))] = true
                                }
                            }
                        }
                    }
                    WinCondition.DIAGONAL_2 -> {
                        Log.d("Test", "${winCoordinates!!.first.second}:${winCoordinates.second.first}")
                        Log.d("Test", "${winCoordinates!!.first.first}:${winCoordinates.second.second}")
                        for(i in winCoordinates!!.first.first .. winCoordinates.second.first) {
                            for (j in winCoordinates!!.second.second..winCoordinates.first.second) {
                                if(i == -j + gd.getMinimumWin() - 1){
                                    this.winIndices[positionConverter1D(Point(i, j))] = true
                                }
                            }
                        }
                    }
                    else -> {
                        val constraints = gd.getBoard().getConstraints()
                        this.winIndices = Array(constraints.first*constraints.second, { false })
                    }
                }
            }
        }
        timerStop()
        print1D(this.winIndices as Array<Any>)
        Log.d("Test", "${this.winner}")
    }

    /** markerPlaced
     * > Places a marker when a cell is selected in the UI.
     * > tests the winCondition
     *          > queries winCondition- if a win, calls this.winnerDecided
     */
    private fun markerPlaced(position: Int){
        if(this.winner != null){
            return
        }
        Log.v(TAG, "Placing marker for ${gd.whoIsPlaying()?.playerName}")
        val position2D = positionConverter(position)
        Log.v(TAG, TYPE+"MarkerPlaced: UIPosition = $position, GameDriverPosition = ${position2D.x},${position2D.y}")
        /** Player 1 makes move **/
        var wincondition = gd.playMove(position2D.x,position2D.y)
        /** if Player 1 wins **/
        when(wincondition){
            WinCondition.HORISONTAL,
            WinCondition.VERTICAL,
            WinCondition.DIAGONAL_1,
            WinCondition.DIAGONAL_2 -> {
                Log.v(TAG, "Win condition detected for player.")
                val board2D = gd.getBoardAsString()
                boardConvertAndSet(board2D)
                print2D(board2D)
                val wincoordinates = gd.getWinCoordinates(wincondition)
                this.winnerDecided(gd.whoIsPlaying(), wincondition, wincoordinates)

                // Update win status for UI
                if (gd.whoIsPlaying() == player1) {
                    setPlayerAsWin(0)
                } else {
                    setPlayerAsWin(1)
                }

                GameEnd()

                return
            }
            WinCondition.DRAW -> {
                Log.v(TAG, "Draw detected for player.")
                this.winnerDecided(null, null,null)

                // Update board at end of game
                val board2D = gd.getBoardAsString()
                boardConvertAndSet(board2D)

                // Set win status for UI updates
                player1WinStatus = PLAYERWINSTATUS.DRAW
                player2WinStatus = PLAYERWINSTATUS.DRAW
                GameEnd()
                return
            }
            else -> {
                this.winner = null
                Log.v(TAG, "No win condition detected for player.")
                swapPlayer()
            }
        }
        /** If next player is AI -> play AI move automatically **/
        if (player2 is AIPlayer){
            markerPlacedAI()
        }
        updateNewMarker()

        movesMade += 1

        // if AI (Single player game) undo is always unavailable
        undoAvailable = if(player2 is AIPlayer){
            false
        }else{
            movesMade >= 2 // Sets undo to true if 2 or more moves
        }
    }

    // Sets p1/p2 with the appropriate win status for UI updates
    // Called in MarkerPlaced
    private fun setPlayerAsWin(playerNum: Int) {
        if (playerNum == 0) {
            player1WinStatus = PLAYERWINSTATUS.WIN
            player2WinStatus = PLAYERWINSTATUS.LOSS
        } else {
            player1WinStatus = PLAYERWINSTATUS.LOSS
            player2WinStatus = PLAYERWINSTATUS.WIN
        }
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
        if (moveUndoAvailable != null){
            undoAvailable = true
        }
        player1Timer = 10
        player2Timer = 10
    }

    /** markerPlacedAI
     * > Places marker for AI player.
     * > Called from markerPlaced if in single player
     */
    private fun markerPlacedAI(){
        val wincondition = gd.playMove()

        // Update board as soon as possible
        val board2D = gd.getBoardAsString()
        boardConvertAndSet(board2D)

        /** If the AI player wins **/
        when(wincondition){
            WinCondition.HORISONTAL,
            WinCondition.VERTICAL,
            WinCondition.DIAGONAL_1,
            WinCondition.DIAGONAL_2 -> {
                Log.v(TAG, "Win condition detected for AI.")
                val wincoordinates = gd.getWinCoordinates(wincondition)
                val board2D = gd.getBoardAsString()
                boardConvertAndSet(board2D)
                print2D(board2D)
                this.winnerDecided(gd.whoIsPlaying(), wincondition, wincoordinates)

                // Update win status for UI
                if (gd.whoIsPlaying() == player1) {
                    setPlayerAsWin(0)
                } else {
                    setPlayerAsWin(1)
                }

                GameEnd()
                return
            }
            WinCondition.DRAW -> {
                Log.v(TAG, "Draw detected for AI player.")
                this.winnerDecided(null, null,null)

                // Update board at end of game
                val board2D = gd.getBoardAsString()
                boardConvertAndSet(board2D)

                // Set win status for UI updates
                player1WinStatus = PLAYERWINSTATUS.DRAW
                player2WinStatus = PLAYERWINSTATUS.DRAW
                GameEnd()

                return
            }
            else -> {
                this.winner = null
                Log.v(TAG, "No win condition detected for AI.")
                swapPlayer()
            }
        }
    }

    /********************************
     ** Helper functions **
     *******************************/

    private fun swapPlayer(){
        Log.v(TAG, "Swapping player")
        if(player1Turn){
            player1Turn = false
            player2Turn = true
            Log.v("Test", "Player 2 Turn")
        }else{
            player1Turn = true
            player2Turn = false
            Log.v("Test", "Player 1 Turn")
        }
        this.gd.nextPlayer()
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
    fun getMarkerSymbol(stateMarker: Int): String{
        return if(stateMarker == 0){
            "x"
        } else{
            "o"
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
                /**
                 * If multiplayer game, someones timer run out, and nobody has won
                 *      -> swap players, and reset timers
                 * else if singleplayer game, player 2 is AI, player 1 timer is out, and nobody won
                 *      -> play AI player and reset timers
                 */
                if (timerActive
                    && (!singlePlayerGame)
                    && (player1Timer == 0 || player2Timer == 0)
                    && (winCondition == WinCondition.NO_WIN)){
                    Log.v("Test", "player timed out")
                    this.swapPlayer()
                    player1Timer = 10
                    player2Timer = 10
                } else if (timerActive
                    && (singlePlayerGame)
                    && (player2 is AIPlayer)
                    && (player1Timer == 0)
                    && (winCondition == WinCondition.NO_WIN)){
                    this.markerPlacedAI()
                    this.updateNewMarker()
                    player1Timer = 10
                    player2Timer = 10
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