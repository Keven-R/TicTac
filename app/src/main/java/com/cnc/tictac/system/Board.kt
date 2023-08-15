package com.cnc.tictac.system
import java.util.Stack
// Board state is a nullable 2D array of players. Initially a null array.
typealias BoardState = Array<Array<Player?>>
class Board(
    private val width           : Int,
    private val height          : Int,
    private val minimumWin      : Int,
    var boardState              : BoardState = Array(width, { Array(height, { null }) }),
    var boardHistory            : Stack<BoardState>
){
    /** init
     * Appends empty board state to board history
     */
    init{
        // Append initial blank board state to boardHistory stack
        boardHistory.push(boardState)
    }
    /** placePuck ( currentPlayer , x, y )
     * ------------------------------------
     *  This method places the player object reference in the specified x and y coordinates of the board.
     *  Understanding which player has the turn, and other such game logic is controlled by the
     *  GameController class.
     */
    fun placePuck(currentPlayer : Player, x : Int, y : Int){
        this.boardState[x][y] = currentPlayer
        this.boardHistory.push(this.boardState)
    }

    /** undoPreviousMove (void)
     * -------------------------
     * This method pops the boardHistory (removing the latest entry),
     * Then sets the boardstate to the top of the boardHistory stack.
     */
    fun undoPreviousMove(){
        this.boardHistory.pop()
        this.boardState = this.boardHistory.peek()
    }

    /** clearGameBoard()
     * Calling this method clears the boardState to null, and empties the boardHistory stack.
     */
    fun clearGameBoard(){
        // Create board state of 2D array of Players (a nullable type).
        this.boardState = Array(this.width, { Array(this.height, { null }) })
        // Append initial blank board state to boardHistory stack
        this.boardHistory.clear()
        this.boardHistory.push(this.boardState)
    }
}