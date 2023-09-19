package com.cnc.tictac.backend.gamedriver
/**********************************
 * Ryan May: 19477774. 08/23
 * PLEASE SEE THE GAMEDRIVER_README DOCUMENT ON GITHUB FOR MORE INFORMATION.
 **********************************/
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.cnc.tictac.backend.database.PLAYER_DAO
import com.cnc.tictac.backend.database.PLAYER_ROOM_DATABASE
import com.cnc.tictac.backend.system.AIPlayer
import com.cnc.tictac.backend.system.Board
import com.cnc.tictac.backend.system.HumanPlayer
import com.cnc.tictac.backend.system.Player
import com.cnc.tictac.backend.system.WinCondition
import java.lang.Math.round

private const val TAG = "Game Driver"

/** Organisation of the functions in this file are as follows:
 *
 *   (1) Class Variables
 *
 *   (2) Game Configuration
 *          (a) reinit: update game configuration
 *          (b) setPlayerOrder: change who plays first
 *          (c) setFirstPlayer: set the first player in the game
 *          (d) setSecondPlayer
 *          (e) resetGameDriver: all attributes of game driver are reset to initial configuration
 *          (f) resetGameBoard: board is cleared
 *
 *   (3) Game Accessors
 *          (a) whoIsPlaying: returns Player object currently making a move
 *          (b) getBoard: returns 2D array of Player objects
 *          (c) getBoardAsString: returns 2D array of player icons (Strings)
 *          (d) getCopyOfPlayerList: returns list of players
 *          (e) getPlayerArray: returns direct reference to player array
 *
 *   (4) Game Mechanics
 *          (a) playMove: will also check for win condition and return a win condition enum value
 *          (b) resetGameBoard
 *          (c) undoPreviousMove
 *
 *   (5) Database Mutators
 *          (a) editPlayerAttributeInDatabase
 *          (b) removePlayerFromDatabase
 *          (c) updatePlayerStatsInDatabase
 *          (d) addPlayerToDatabase
 *
 *   (6) Database Accessors
 *          (a) getPlayerFromDatabase
 *          (b) getPlayersFromDatabase: returns all players
 *          (c) getPlayerStatsFromDatabase
 *          (d) getPlayerDisplayStatsFromDatabase: returns player stats in three neatly formatted strings
 *          (e) getPlayerStatsRibbonFromDatabase: "//xxxoooo" formatted string
 *          (f) getLeaderboard : returns list of players in descending order of wins.
 */

class GameDriver(
    config : GameConfig = GameConfig(),
    db : PLAYER_ROOM_DATABASE?
) {
    /**___________________________________
                Class Variables
     _____________________________________**/
    // Backend
    private var board           : Board
    private var playerArray     : Array<Player?>    = arrayOf(HumanPlayer(), HumanPlayer())
    private var currentPlayer   : Int               = 0
    private var player          : Player?           = null
    // Driver
    private var playerDAO       : PLAYER_DAO
    private var playerDB        : PLAYER_ROOM_DATABASE? = null
    /**********************************
     *  init()
     **********************************
     *  > Constructs board object using game config file given
     *  > Establishes context for room database
     *  > Builds room database
     *  > Fetches DAO for room database.
     */
    init{
        Log.d(TAG, "Initialising the game driver.")
        Log.d(TAG, "Creating Board object.")
        board = Board(
            config.boardWidth,
            config.boardHeight,
            config.boardMinimumWin,
            )
        Log.d(TAG, "setting player DB from parameter.")
        this.playerDB = db
        Log.d(TAG, "Obtaining room database DAO.")
        this.playerDAO = this.playerDB!!.getDAO()
        Log.d(TAG, "Game driver successfully initialised.")
    }
    /**___________________________________
                Game Configuration
    _____________________________________**/
    /**********************************
     * Update game driver configuration
     **/
    fun reinit(config : GameConfig){
        Log.d(TAG, "Reinitialising the game driver with updated game settings.")
        board = Board(
            config.boardWidth,
            config.boardHeight,
            config.boardMinimumWin,
        )
    }
    /**********************************
     * setPlayerOrder(toggle : Int)
     * sets the player order by changing the starting value of the current player (0 or 1).
     * players (for this game) are stored in an array of length 2.
     **/
    fun setPlayerOrder(toggle : Int){
        this.currentPlayer = toggle
    }
    /**********************************
     * setFirstPlayer(player : Player? = HumanPlayer())
     * setSecondPlayer(player : Player? = HumanPlayer())
     * Sets the first and second player in the game.
     */
    fun setFirstPlayer(newPlayer : Player = HumanPlayer()){
        /** Debugging output **/
        when (newPlayer) {
            is HumanPlayer -> { Log.d(TAG, "Adding HumanPlayer to game.") }
            is AIPlayer -> { Log.d(TAG, "Adding AIPlayer to game.") }
            else -> { Log.e(TAG, "Player added to game is of unknown child class (!!).") }
        }
        this.playerArray[0] = newPlayer
    }
    fun setSecondPlayer(newPlayer : Player = HumanPlayer()){
        /** Debugging output **/
        when (newPlayer) {
            is HumanPlayer -> { Log.d(TAG, "Adding HumanPlayer to game.") }
            is AIPlayer -> { Log.d(TAG, "Adding AIPlayer to game.") }
            else -> { Log.e(TAG, "Player added to game is of unknown child class (!!).") }
        }
        this.playerArray[1] = newPlayer
    }
    /********************************
     * resetGameDriver()
     ********************************
     * This method resets the game driver by clearing the game board, emptying the player
     * queue, setting the player count to 0, and setting the current player to NULL.
     */
    fun resetGameDriver(){
        Log.d(TAG, "Game driver is being reset.")
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
        Log.d(TAG, "Game config is being updated to ${config.boardWidth} by ${config.boardHeight}.")
        this.board.clearGameBoard()
        this.board = Board(config.boardWidth, config.boardHeight, config.boardMinimumWin)
    }
    /**---------------------------------
            Game Accessors
     -----------------------------------**/
    /**********************************
     * whoIsPlaying() : Player ?
     ***********************************
     *  returns the current player in the queue
     */
    fun whoIsPlaying() : Player ? {
        Log.d(TAG, "Current player is ${this.playerArray[currentPlayer]?.playerName}")
        return this.playerArray[currentPlayer]
    }
    /**********************************
     *  getBoard()
     **********************************
     *  accessor for the board
     */
    fun getMinimumWin() : Int {
        return this.getBoard().getMinimumWin()
    }
    fun getBoard() : Board {
        Log.d(TAG, "Current board is being fetched.")
        return this.board
    }
    fun getBoardAsString() : Array<Array<String>>{
        Log.d(TAG, "Getting board as array string.")
        val board = this.getBoard()
        val constraint1 = board.getConstraints().second
        val constraint2 = board.getConstraints().first
        Log.d(TAG, "Board constraints are $constraint1, $constraint2")
        val boardString = Array(constraint1, { Array(constraint2, { "" }) })
        for(i in 0 until constraint1){
            for(j in 0 until constraint2){
                if(board.getBoardState()[i][j] != null) {
                    boardString[i][j] = board.getBoardState()[i][j]!!.playerIcon
                } else {
                    boardString[i][j] = " "
                }
            }
        }
        return boardString
    }
    /**********************************
     *  getCopyOfPlayerList()
     **********************************
     *  accessor for the player Queue
     */
    fun getCopyOfPlayerList() : List<Player?> {
        Log.d(TAG, "Copy of player queue is being returned.")
        return this.playerArray.toList()
    }
    fun getPlayerArray() : Array<Player?> {
        Log.d(TAG, "Player array is being returned.")
        return this.playerArray
    }
    /**___________________________________
                Game Mechanics
    _____________________________________**/
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
    fun playMove(x : Int? =  null, y : Int? =  null) : WinCondition? {
        Log.d(TAG, "Placing a puck at $x, $y.")
        /** Get current player **/
        this.player = this.playerArray[currentPlayer]
        Log.d(TAG, "Current player is ${this.player?.playerName} : ${this.player?.playerIcon}")
        /** Debugging output **/
        when (this.player) {
            is HumanPlayer -> { Log.d(TAG, "Adding HumanPlayer to game.") }
            is AIPlayer -> { Log.d(TAG, "Adding AIPlayer to game.") }
            else -> { Log.e(TAG, "Player added to game is of unknown child class (!!).") }
        }
        /** Check if player is AI and get new coordinates if AI **/
        if(this.player is AIPlayer){
            do {
                var aiX : Int? = x
                var aiY : Int? = y
                if(x == null || y == null) {
                    aiX = (this.player as AIPlayer).generateRandomPlay(this.board.getConstraints()).first
                    aiY = (this.player as AIPlayer).generateRandomPlay(this.board.getConstraints()).second
                }
                Log.d(TAG, "AIPlayer is placing a puck at $aiX, $aiY")
            }while(!this.board.placePuck(x = aiX!!, y = aiY!!, currentPlayer = this.player!!))
            // Random plays will continue being made until a square without a puck is found.
            Log.d(TAG, "Searching win condition for AIPlayer")
            return this.board.searchWinCondition(this.player!!)
        } else if(this.player is HumanPlayer && x != null && y != null) { /** Placing puck if HumanPlayer **/
            /** placing a puck **/
            Log.d(TAG, "HumanPlayer is placing a puck at $x, $y")
            if (!this.board.placePuck(x = x!!, y = y!!, currentPlayer = this.player!!)) {
                Log.e(TAG, "Puck is placed on occupied square.")
                return null
            }
            Log.d(TAG, "Searching win condition for HumanPlayer")
            return this.board.searchWinCondition(this.player!!)
        } else {
            return null
        }
    }
    fun nextPlayer() {
        Log.d(TAG, "Next player")
        if(this.currentPlayer == 0){
            this.currentPlayer = 1
        } else {
            this.currentPlayer = 0
        }
    }
    fun getWinCoordinates(winCondition : WinCondition) : Pair<Pair<Int, Int>, Pair<Int, Int>>?{
        Log.d(TAG, "Getting win coordinates")
        if(this.player != null) {
            return this.board.getWinCoordinates(this.player!!, winCondition)
        }
        return null
    }
    /**********************************
     * resetGameBoard()
     **********************************
     * This method clears the game board.
     */
    fun resetGameBoard(){
        Log.d(TAG, "Game driver is clearing game board.")
        this.board.clearGameBoard()
    }
    /**********************************
     * undoPreviousMove()
     **********************************
     * Undoes the last move played in the game.
     */
    fun undoPreviousMove(){
        Log.d(TAG, "Game driver is reversing previous move.")
        if(this.currentPlayer == 0){
            this.currentPlayer = 1
        } else {
            this.currentPlayer = 0
        }
        this.board.undoPreviousMove()
    }
    /**___________________________________
                Database Mutators
    _____________________________________**/
    /**********************************
     * editPlayerDatabaseAttribute(player : Player, attribute : String, value : Any) : Boolean
     *********************************
     *  Allows for the editing of a player in the player database.
     *  If an error occurs (ie: A player ID is altered to a value already in the database),
     *      false is returned.
     */
    fun editPlayerDatabaseAttribute(player : Player, attribute : String, value : Any) : Boolean{
        Log.d(TAG, "Player attribute $attribute changing to $value.")
        // Saving player before attribute editing; revert back if error
        val playerCopy : Player = player.copy()
        // removing the player from the database will clear the player stats, so we save them first.
        val statsTriplet = this.getPlayerStatsFromDatabase(player as HumanPlayer)
        // removing the player
        this.removePlayerFromDatabase(player.playerID)
        when(attribute) {
            "playerName" -> player.playerName = value as String
            "playerID" -> player.playerID = value as Int
            "playerAvatar" -> player.playerAvatar = value as Int
            "playerIcon" -> player.playerIcon = value as String
        }
        // Adding modified player
        if(!this.addPlayerToDatabase(player)){
            // If the player ID is modified to a value already in the database, false is returned and...
            Log.e(TAG, "Attempted to edit player attibute - PlayerID is already occupied(!!!)")
            // Changes are abandoned
            this.addPlayerToDatabase(playerCopy as HumanPlayer)
            return false
        }
        // amend the player stats back onto the new player we created.
        this.updatePlayerStatsInDatabase(player,
            wins = statsTriplet.second,
            losses = statsTriplet.first,
            draws = statsTriplet.third)
        return true
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
     * updatePlayerStatsInDatabase(...)
     * Allows updating player stats in the room ROOM_PLAYER_DATABASE of the game driver.
     *********************************/
    fun updatePlayerStatsInDatabase(player : HumanPlayer, wins : Int?, draws : Int?, losses : Int?){
        Log.d(TAG, "Updating player ${player.playerName} to database.")
        if(wins != null) {
            this.playerDAO.updateWins(wins, player.playerID!!)
        }
        if(draws != null) {
            this.playerDAO.updateDraws(draws, player.playerID!!)
        }
        if(losses != null) {
            this.playerDAO.updateLosses(losses, player.playerID!!)
        }
    }
    /**********************************
     * addPlayerToDatabase(player : Player? = HumanPlayer()) : Boolean
     **********************************
     *  This methods adds a player to the database.
     *  If the player is human, player is added and true is returned.
     *  Otherwise, false is returned.
     */
    fun addPlayerToDatabase(newPlayer : Player? = HumanPlayer()) : Boolean{
        Log.d(TAG, "Adding player to database.")
        if(newPlayer is HumanPlayer) {
            try {
                this.playerDAO.addNewPlayer(
                    newPlayer.playerID,
                    newPlayer.playerName,
                    newPlayer
                )
            }catch(exception : SQLiteConstraintException){
                Log.e(TAG, "Player ID already exists in database (!!).")
                return false
            }
        }
        return false
    }
    /**___________________________________
                Database Accessors
    _____________________________________**/
    /**********************************
     * getPlayersFromDatabase() : List<HumanPlayer?>
     **********************************
     *  Returns a list of all HumanPlayers in the player database.
     */
    fun getPlayersFromDatabase() : List<HumanPlayer?> {
        Log.d(TAG, "Player objects being pulled from database.")
        return this.playerDAO.getAllPlayers()
    }
    /**********************************
     * getPlayerFromDatabase(playerID : Int) : HumanPlayer?
     **********************************
     *  Returns a list of all HumanPlayers in the player database.
     */
    fun getPlayerFromDatabase(playerID : Int?) : HumanPlayer? {
        Log.d(TAG, "Player object by ID $playerID being pulled from database.")
        return this.playerDAO.selectPlayerbyID(playerID)
    }
    /**********************************
     * getPlayerStatsFromDatabase(player : Player) : Tripple<Losses, Wins, Draws>
     **********************************
     * Fetch WLD statistics from the database for a specific player, searched by player ID.
     */
    fun getPlayerStatsFromDatabase(player : HumanPlayer) : Triple<Int, Int, Int>{
        Log.d(TAG, "Obtaining player game stats from database.")
        val losses = this.playerDAO.getLosses(player.playerID)
        val wins = this.playerDAO.getWins(player.playerID)
        val draws = this.playerDAO.getDraws(player.playerID)
        Log.d(TAG, "${player.playerName} has stats wins = $wins, looses = $losses, draws = $draws.")
        return Triple(wins, losses, draws)
    }

    fun getPlayerTotalGamesFromDatabase(player : HumanPlayer) : Int {
        Log.d(TAG, "Obtaining player total games from database.")
        val losses = this.playerDAO.getLosses(player.playerID)
        val wins = this.playerDAO.getWins(player.playerID)
        val draws = this.playerDAO.getDraws(player.playerID)
        val total = losses+wins+draws
        Log.d(TAG, "${player.playerName} has total games: $total")
        return total
    }
    /**
     * A duplicate of the above method, returning a formatted string in percent. Here to allow easy
     * display in the frontend.
     */
    fun getPlayerDisplayStatsFromDatabase(player : HumanPlayer) : Triple<String, String, String>{
        Log.d(TAG, "Obtaining player game stats from database as string")
        val losses  = this.playerDAO.getLosses(player.playerID).toFloat()
        val wins    = this.playerDAO.getWins(player.playerID).toFloat()
        val draws   = this.playerDAO.getDraws(player.playerID).toFloat()
        val games   = wins + draws + losses
        val lossesPercent  : Int = round((losses) / (games) * 100)
        val winsPercent    : Int = round((wins) / (games) * 100)
        val drawsPercent   : Int = round((draws) / (games) * 100)
        return Triple("wins: ${wins.toInt()} (${winsPercent}%)",
            "draws: ${draws.toInt()} (${drawsPercent}%)",
            "losses: ${losses.toInt()} (${lossesPercent}%)")
    }
    // Quick add by Keven to make a display string easier to get
    fun getPlayerTotalGamesDisplayFromDatabase(player : HumanPlayer) : String{
        Log.d(TAG, "Obtaining player total game stats from database.")
        val losses = this.playerDAO.getLosses(player.playerID)
        val wins = this.playerDAO.getWins(player.playerID)
        val draws = this.playerDAO.getDraws(player.playerID)
        val total = losses + wins + draws
        Log.d(TAG, "${player.playerName} has a total of $total games.")
        return "total games: $total"
    }
    /**********************************
     * getPlayerStatsRibbon()
     * Returns a sylistic representation of the wins, losses are draws of the player.
     * /////xxooo = 5 draws, 2 wins, 3 losses.
     * Length of returned string is 10.
     *********************************/
    fun getPlayerStatsRibbonFromDatabase(player : HumanPlayer) : String {
        var returnString = ""
        val losses  = this.playerDAO.getLosses(player.playerID).toFloat()
        val wins    = this.playerDAO.getWins(player.playerID).toFloat()
        val draws   = this.playerDAO.getDraws(player.playerID).toFloat()
        val games   = wins + draws + losses
        val lossesFraction : Int = round(losses / games * 10)
        val winsFraction   : Int = round(wins   / games * 10)
        val drawsFraction  : Int = round(draws  / games * 10)
        Log.d(TAG, "Displaying $lossesFraction Os, $drawsFraction /s, $winsFraction Xs")
        for(i in 0 ..winsFraction-1)
            returnString += "o"
        for(i in 0 .. drawsFraction-1)
            returnString += "/"
        for(i in 0 .. lossesFraction-1)
            returnString += "x"
        return returnString
    }
    fun getLeaderboard() : List<HumanPlayer?> {
        return this.playerDAO.getLeaderboard()
    }
}