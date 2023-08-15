package com.cnc.tictac.system
import java.util.Stack

// Board state is a nullable 2D array of players. Initially a null array.
typealias BoardState = Array<Array<Player?>>
class Board(
    var boardState      : BoardState,
    var boardHistory    : Stack<BoardState>,
    private val width           : Int,
    private val height          : Int,
    private val minimumWin      : Int,
){
    init{
        // Create board state of 2D array of Players (a nullable type).
        boardState = Array(width, { Array(height, { null }) })
        // Append initial blank board state to boardHistory stack
        boardHistory.add(boardState)
    }
    /** placePuck ( currentPlayer , x, y )
     *  This method places the player object reference in the specified x and y coordinates of the board.
     *  Understanding which player has the turn, and other such game logic is controlled by the
     *  GameController class.
     */
    fun placePuck(currentPlayer : Player, x : Int, y : Int){

    }
}