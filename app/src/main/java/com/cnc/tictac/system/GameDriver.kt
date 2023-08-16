package com.cnc.tictac.system
/**
 * PLEASE SEE THE GAMEDRIVER_README DOCUMENT ON GITHUB FOR MORE INFORMATION.
 */
import java.util.LinkedList
import java.util.Queue
data class GameConfig(
    // Board Configurations
    val boardHeight     : Int = 3,
    val boardWidth      : Int = 3,
    val boardMinimumWin : Int = 3,
)
class GameDriver(
    config : GameConfig = GameConfig()
) {
    private var board           : Board
    private var playerQueue     : Queue<Player?> = LinkedList<Player?>()
    private var currentPlayer   : Player? = null
    private var playerCount     : Int = 0
    /** init
     *  Create the Board object for the game, and initialise the CircularArray that stores the
     *  players.
     *  The Queue is chosen so that when the driver pulls a player from a queue, it performs that
     *  players' turn, and then appends the player to the end of the queue. Creating an infinite
     *  circular queue.
     */
    init{
        board = Board(
            config.boardWidth,
            config.boardHeight,
            config.boardMinimumWin,
            )
    }
    /** playMove()
     *  Player is removed from the front of the queue, and then added to the rear of the queue.
     *
     *  if coordiantes (x, y) are NOT given, the function checks to see if the currentPlayer is AI.
     *      If it is, it generates a play, if not, it assumes you intened to place a puck at 0,0
     *
     *  The AI generated position is generated within the context of the board constrains from
     *      getBoardConstraints.
     *
     *  In order to test if the current player is AI without removing it, .peek() is used.
     */
    fun playMove(x : Int = if(this.playerQueue.peek()!! is AIPlayer) (this.playerQueue.peek()!! as AIPlayer).generateRandomPlay(this.board.getConstraints()).first else 0 ,
                 y : Int = if(this.playerQueue.peek()!! is AIPlayer) (this.playerQueue.peek()!! as AIPlayer).generateRandomPlay(this.board.getConstraints()).second else 0 ,
                 ) : Pair<String, Boolean>{
        // The logic above handles AI players and generated their own moves.
        // Cycle the currentPlayer queue
        this.currentPlayer = this.playerQueue.remove()
        this.playerQueue.add(this.currentPlayer)
        // play the move
        this.board.placePuck(x = x, y = y, currentPlayer = this.currentPlayer!!)
        return this.board.searchWinCondition(this.currentPlayer!!)
    }
    /** addPlayer()
     *  This methods adds a player to the GameDriver. If the player is NULL, false is returned.
     *  Otherwise, true is returned.
     *  playercount is increased.
     */
    fun addPlayer(newPlayer : Player?) : Boolean{
        return if(newPlayer != null) {
            this.playerQueue.add(newPlayer)
            this.playerCount ++
            true
        } else {
            false
        }
    }
    /** whoIsPlaying() : Player ?
     *  returns the current player
     */
    fun whoIsPlaying() : Player ? {
        return this.playerQueue.peek()
    }
    /** getBoard()
     *  accessor for the board
     */
    fun getBoard() : Board {
        return this.board
    }
    /** getPlayerQueue()
     *  accessor for the player Queue
     */
    fun getPlayerQueue() : Queue<Player?> {
        return this.playerQueue
    }
}