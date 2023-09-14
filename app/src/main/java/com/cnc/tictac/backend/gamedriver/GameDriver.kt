package com.cnc.tictac.backend.gamedriver
/**********************************
 * Ryan May: 19477774. 08/23
 * PLEASE SEE THE GAMEDRIVER_README DOCUMENT ON GITHUB FOR MORE INFORMATION.
 **********************************/
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cnc.tictac.backend.database.PLAYER_DAO
import com.cnc.tictac.backend.database.PLAYER_ROOM_DATABASE
import com.cnc.tictac.backend.system.AIPlayer
import com.cnc.tictac.backend.system.Board
import com.cnc.tictac.backend.system.HumanPlayer
import com.cnc.tictac.backend.system.Player
import com.cnc.tictac.backend.system.WinCondition

class GameDriver(
    config : GameConfig = GameConfig()
) {
/**********************************
 * Class Variables
 **********************************/
    // Backend
    private var board           : Board
    private var playerArray     : Array<Player?>    = arrayOf(HumanPlayer(), HumanPlayer())
    private var currentPlayer   : Int               = 0
    private var player          : Player?           = null
    // Driver
    private var playerDAO: PLAYER_DAO
    private var db: PLAYER_ROOM_DATABASE
    /**********************************
     *  init()
     **********************************
     *  > Constructs board object using game config file given
     *  > Establishes context for room database
     *  > Builds room database
     *  > Fetches DAO for room database.
     */
    init{
        Log.d("<GAME_DRIVER>", "Initialising the game driver.")
        board = Board(
            config.boardWidth,
            config.boardHeight,
            config.boardMinimumWin,
            )
        val context = ApplicationProvider.getApplicationContext<Context>()
        this.db = Room.databaseBuilder(
            context,
            PLAYER_ROOM_DATABASE::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()
            .also {
                Log.d("<GAME_DRIVER>", "Building room database.")
                it.openHelper.writableDatabase.path?.let { it1 -> Log.d("<GAME_DRIVER>", it1) }
            }
        this.playerDAO = this.db.getDAO()
        Log.d("<GAME_DRIVER>", "Game driver successfully initialised.")
    }
/********************
 * GAME MECHANICS   *
 ********************/
    /**********************************
     * playMove(x, y : Int) : WinCondition
     **********************************
     *  Player is removed from the front of the queue, and then added to the rear of the queue.
     *  if coordiantes (x, y) are NOT given, the function checks to see if the currentPlayer is AI.
     *      If it is, it generates a play, if not, it assumes you intened to place a puck at 0,0
     *  The AI generated position is generated within the context of the board constrains from
     *      getBoardConstraints.
     *  In order to test if the current player is AI without removing it, .peek() is used.
     *  Returns a WinCondition enum attribute.
     */
    fun playMove(x : Int =  0,
                 y : Int =  0,
                 ) : WinCondition{
        Log.d("<GAME_DRIVER>", "Placing a puck at $x, $y.")
        // The logic above handles AI players and generated their own moves.

        /** Toggle current player **/
        if(this.currentPlayer == 0){
            this.currentPlayer = 1
        } else {
            this.currentPlayer = 0
        }
        /** Get current player **/
        this.player = this.playerArray[currentPlayer]

        /** Check if player is AI and get new coordinates if AI **/
        if(this.player is AIPlayer){
            var x = (this.player as AIPlayer).generateRandomPlay(this.board.getConstraints()).first
            var y = (this.player as AIPlayer).generateRandomPlay(this.board.getConstraints()).second
        }

        /** Debugging output **/
        if(this.player is HumanPlayer) {Log.d("<GAME_DRIVER>", "HumanPlayer is placing a puck.")}
        else if(this.player is AIPlayer) { Log.d("<GAME_DRIVER>", "AIPlayer is placing a puck.") }
        else { Log.d("<GAME_DRIVER>", "Player child object is of unknown class(!!!).") }

        /** placing a puck **/
        if(!this.board.placePuck(x = x, y = y, currentPlayer = this.player!!)) {
            Log.d("<GAME_DRIVER>", "Exception thrown: Puck is placed on unknown square.")
            throw Exception("Puck placed on occupied square")
        }
        return this.board.searchWinCondition(this.player!!)
    }
    /**********************************
     * undoPreviousMove()
     **********************************
     * Undoes the last move played in the game.
     */
    fun undoPreviousMove(){
        Log.d("<GAME_DRIVER>", "Game driver is reversing previous move.")
        this.board.undoPreviousMove()
    }
    /**********************************
     * resetGameBoard()
     **********************************
     * This method clears the game board.
     */
    fun resetGameBoard(){
        Log.d("<GAME_DRIVER>", "Game driver is clearing game board.")
        this.board.clearGameBoard()
    }
    /********************************
     * resetGameDriver()
     ********************************
     * This method resets the game driver by clearing the game board, emptying the player
     * queue, setting the player count to 0, and setting the current player to NULL.
     */
    fun resetGameDriver(){
        Log.d("<GAME_DRIVER>", "Game driver is being reset.")
        this.board.clearGameBoard()
        this.playerArray = arrayOf(HumanPlayer(), HumanPlayer())
        this.currentPlayer = 0
        this.player = null
    }
    /*****************************
     * createNewBoard(config : GameConfig)
     *****************************
     * this creates a new board for the game given a specific game config data object
     */
    fun createNewBoard(config : GameConfig){
        Log.d("<GAME_DRIVER>", "Game config is being updated to ${config.boardWidth} by ${config.boardHeight}.")
        this.board.clearGameBoard()
        this.board = Board(config.boardWidth, config.boardHeight, config.boardMinimumWin)
    }
 /**********************
 * PLAYER OPERATIONS  *
 **********************/
    /**********************************
     * editPlayerAttribute(player : Player, attribute : String, value : Any)
     *********************************
     *  Allows for the editing of a player in the player database.
     */
    fun editPlayerAttribute(player : Player, attribute : String, value : Any){
        Log.d("<GAME_DRIVER>", "Player attribute $attribute changing to $value.")
        when(attribute){
            "playerName"    -> player.playerName = value as String
            "playerID"      -> player.playerID = value as Int
            "playerAvatar"  -> player.playerAvatar = value as Int
            "playerIcon"    -> player.playerIcon = value as String

        }
        playerDAO.removePlayer(player.playerID)
        playerDAO.addNewPlayer(player.playerID, player.playerName, player as HumanPlayer)
    }
    /**********************************
     * removePlayerFromDatabase(playerID : Int)
     **********************************
     *  Removes a player from a database.
     */
    fun removePlayerFromDatabase(playerID : Int?){
        playerDAO.removePlayer(playerID)
    }
    /**********************************
     * getPlayersFromDatabase() : List<HumanPlayer?>
     **********************************
     *  Returns a list of all HumanPlayers in the player database.
     */
    fun getPlayersFromDatabase() : List<HumanPlayer?> {
        Log.d("<GAME_DRIVER>", "Player objects being pulled from database.")
        return this.playerDAO.getAllPlayers()
    }
    /**********************************
     * getPlayerFromDatabase(playerID : Int) : HumanPlayer?
     **********************************
     *  Returns a list of all HumanPlayers in the player database.
     */
    fun getPlayerFromDatabase(playerID : Int?) : HumanPlayer? {
        Log.d("<GAME_DRIVER>", "Player object by ID $playerID being pulled from database.")
        return this.playerDAO.selectPlayerbyID(playerID)
    }
    /**********************************
     * getPlayerStatsFromDatabase(player : Player) : Tripple<Losses, Wins, Draws>
     **********************************
     * Fetch WLD statistics from the database for a specific player, searched by player ID.
     */
    fun getPlayerStatsFromDatabase(player : HumanPlayer) : Triple<Int, Int, Int>{
        Log.d("<GAME_DRIVER>", "Obtaining player game stats from database.")
        val losses = this.playerDAO.getLosses(player.playerID)
        val wins = this.playerDAO.getWins(player.playerID)
        val draws = this.playerDAO.getDraws(player.playerID)
        Log.d("<GAME_DRIVER>", "${player.playerName} has stats wins = $wins, looses = $losses, draws = $draws.")
        return Triple(losses, wins, draws)
    }
    fun getPlayerDisplayStats(player : HumanPlayer) : Triple<String, String, String>{
        Log.d("<GAME_DRIVER>", "Obtaining player game stats from database as string")
        val losses = this.playerDAO.getLosses(player.playerID)
        val wins = this.playerDAO.getWins(player.playerID)
        val draws = this.playerDAO.getDraws(player.playerID)
        val losses_percent = losses / (wins + losses + draws) * 100
        val wins_percent = wins / (wins + losses + draws) * 100
        val draws_percent = draws / (wins + losses + draws) * 100
        return Triple("Wins: $wins ($wins_percent%)", "Draws: $draws ($draws_percent%)", "Losses $losses ($losses_percent%)")
    }
    fun getPlayerStatsRibbon(player : HumanPlayer) : String {
        var return_string = ""
        val losses = this.playerDAO.getLosses(player.playerID)
        val wins = this.playerDAO.getWins(player.playerID)
        val draws = this.playerDAO.getDraws(player.playerID)
        val losses_fraction = losses / (wins + losses + draws)
        val wins_fraction = wins / (wins + losses + draws)
        val draws_fraction = draws / (wins + losses + draws)
        for(i in 0 .. wins_fraction)
            return_string += "X"
        for(i in 0 .. draws_fraction)
            return_string += "/"
        for(i in 0 .. losses_fraction)
            return_string += "O"
        return return_string
    }
    /**********************************
     * addPlayerToDatabase(player : Player? = HumanPlayer()) : Boolean
     **********************************
     *  This methods adds a player to the database.
     *  If the player is human, player is added and true is returned.
     *  Otherwise, false is returned.
     */
    fun addPlayerToDatabase(newPlayer : Player? = HumanPlayer()) : Boolean{
        Log.d("<GAME_DRIVER>", "Adding player to database.")
        if(newPlayer is HumanPlayer) {
            try {
                this.playerDAO.addNewPlayer(
                    newPlayer.playerID,
                    newPlayer.playerName,
                    newPlayer
                )
                return true
            }catch(exception : SQLiteConstraintException){
                Log.d("<GAME_DRIVER>", "Player ID already exists in database (!!).")
                return false
            }
        }
        return false
    }
    /**********************************
     * addPlayerToGame(player : Player? = HumanPlayer())
     **********************************
     *  Player is added to player queue, player count is incremented.
     */
    fun setFirstPlayer(newPlayer : Player = HumanPlayer()){
        /** Debugging output **/
        if(newPlayer is HumanPlayer) { Log.d("<GAME_DRIVER>", "Adding HumanPlayer to game.") }
        else if(newPlayer is AIPlayer) { Log.d("<GAME_DRIVER>", "Adding AIPlayer to game.") }
        else { Log.d("<GAME_DRIVER>", "Player added to game is of unknown child class (!!).") }
        this.playerArray[0] = newPlayer
    }
    fun setSecondPlayer(newPlayer : Player = HumanPlayer()){
        /** Debugging output **/
        if(newPlayer is HumanPlayer) { Log.d("<GAME_DRIVER>", "Adding HumanPlayer to game.") }
        else if(newPlayer is AIPlayer) { Log.d("<GAME_DRIVER>", "Adding AIPlayer to game.") }
        else { Log.d("<GAME_DRIVER>", "Player added to game is of unknown child class (!!).") }
        this.playerArray[1] = newPlayer
    }
/***************
 * ACCESSORS   *
 ***************/
    /**********************************
     * whoIsPlaying() : Player ?
     ***********************************
     *  returns the current player in the queue
     */
    fun whoIsPlaying() : Player ? {
        Log.d("<GAME_DRIVER>", "Current player is ${this.playerArray[currentPlayer]?.playerName}")
        return this.playerArray[currentPlayer]
    }
    /**********************************
     *  getBoard()
     **********************************
     *  accessor for the board
     */
    fun getBoard() : Board {
        Log.d("<GAME_DRIVER>", "Current board is being fetched.")
        return this.board
    }
    fun getBoardAsString(player1Icon : String, player2Icon : String) : Array<Array<String>>{
        var board = this.getBoard()
        var boardString = Array(board.getConstraints().first, { Array(board.getConstraints().second, { "" }) })
        for(i in 0 .. board.getConstraints().first){
            for(j in 0 .. board.getConstraints().second){
                if(board.getBoardState()[i][j] == this.playerArray[0]){
                    boardString[i][j] = player1Icon
                } else {
                    boardString[i][j] = player2Icon
                }
            }
        }
        return boardString
    }
    /**********************************
     *  getCopyOfPlayerQueue()
     **********************************
     *  accessor for the player Queue
     */
    fun getCopyOfPlayerList() : List<Player?> {
        Log.d("<GAME_DRIVER>", "Copy of player queue is being returned.")
        return this.playerArray.toList()
    }
}