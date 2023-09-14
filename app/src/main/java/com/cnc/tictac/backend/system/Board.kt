package com.cnc.tictac.backend.system
import java.util.Arrays
import java.util.Stack
/**
 * Please See BACKEND_README.md on GITHUB for implementation information.
 */
enum class WinCondition{
    WIN,
    NO_WIN,
    DRAW
}

typealias BoardState = Array<Array<Player?>>
class Board(
    private val width       : Int = 3,
    private val height      : Int = 3,
    private val minimumWin  : Int = 3,
    private var boardState  : BoardState
                            = Array(width, { Array(height, { null }) }),
    private var boardHistory : Stack<BoardState>
                            = Stack(),
    private var movesMade   : Int = 0,
){
    /** init
     * Appends empty board state to board history
     */
    init{
        // Append initial blank board state to boardHistory stack
        boardHistory.push(this.boardState.deepCopy())
    }

    /**
     * Accessor methods
     */
    fun getBoardHistory() : Stack<BoardState> {
        return this.boardHistory
    }
    fun getBoardState() : BoardState {
        return this.boardState
    }
    fun getConstraints() : Pair<Int, Int>{
        return Pair(this.width, this.height)
    }
    /** placePuck ( currentPlayer , x, y )
     * ------------------------------------
     *  This method places the player object reference in the specified x and y coordinates of the board.
     *  Understanding which player has the turn, and other such game logic is controlled by the
     *  GameController class.
     *  If GameController attempts to place a puck out of bounds, ArrayIndexOutOfBoundsException is
     *  caught, and "false" is returned by the function.
     *  Additionally, if the GameController attempts to place a puck in an occupied zone, false is
     *  returned.
     */
    fun placePuck(currentPlayer : Player, x : Int, y : Int) : Boolean{
        try {
            if(this.boardState[x][y] == null){
                this.boardState[x][y] = currentPlayer
                boardHistory.push(this.boardState.deepCopy())
                this.movesMade ++
                return true
            } else {
                return false
            }
        } catch (e : ArrayIndexOutOfBoundsException){
            return false
        }
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
     * -------------------
     * Calling this method clears the boardState to null, and empties the boardHistory stack.
     */
    fun clearGameBoard(){
        // Create board state of 2D array of Players (a nullable type).
        this.boardState = Array(this.width, { Array(this.height, { null }) })
        // Append initial blank board state to boardHistory stack
        this.boardHistory.clear()
        this.boardHistory.push(this.boardState)
    }
    /** searchWinCondition()
     * ----------------------
     *  Performs convolutions to search for win conditions on the board
     *  Modified: 23/08... Allows searching for DRAWS now. Modified output to be an enum, reduces
     *  errors associated with using strings.
     */
    fun searchWinCondition(currentPlayer : Player) : WinCondition{
        // Convolutional templates for calculation
        val convVertical = Array(this.minimumWin, { Array(1, {1}) })
        val convHorisontal = Array(1, { Array(this.minimumWin, {1}) })
        val convDiagonal1 = Array(this.minimumWin, {index1 -> Array(this.minimumWin, {index2 -> if(index1 == index2){ 1 } else{ 0 } })})
        val convDiagonal2 = Array(this.minimumWin, {index1 -> Array(this.minimumWin, {index2 -> if((this.width - index1) == index2){ 1 } else{ 0 } })})

        val positionMap = Array(this.boardState.size, { Array<Int>(this.boardState[0].size, { 0 }) })
        for(y in 0 until this.boardState.size)
            for(x in 0 until this.boardState[0].size)
                positionMap[y][x] = if(this.boardState[x][y] == currentPlayer) {1} else {0}

        val convolutionVertical     = positionMap.convolution2D(convVertical)
        val convolutionHorisontal   = positionMap.convolution2D(convHorisontal)
        val convolutionDiagonal1    = positionMap.convolution2D(convDiagonal1)
        val convolutionDiagonal2    = positionMap.convolution2D(convDiagonal2)

        return if(Arrays.stream(convolutionVertical).anyMatch   { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
            || Arrays.stream(convolutionHorisontal).anyMatch    { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
            || Arrays.stream(convolutionDiagonal1).anyMatch     { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
            || Arrays.stream(convolutionDiagonal2).anyMatch     { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} })
            WinCondition.WIN
        else if(this.movesMade == this.width * this.height)
            WinCondition.DRAW
        else
            WinCondition.NO_WIN
    }
    /** Convolution2D
     * ---------------
     *  Performs 2D convolution between two 2D arrays
     */
    private fun Array<Array<Int>>.convolution2D(operand : Array<Array<Int>>) : Array<Array<Int>>{
        val result = Array(this.size, { Array(this[0].size, { 0 }) })
        for(y in 0 until this.size){
            for(x in 0 until this[0].size){
                //(x, y) is the middle position of the convolution, (OR IT COULD BE THE TOP LEFT, for compat with even squares)
                var sum = 0
                for(y2 in 0 until operand.size){
                    for(x2 in 0 until operand[0].size){
                        //(x2, y2) is the operand sub position
                        sum += this[if(y+y2 < this.size) y+y2 else break][if(x+x2 < this[0].size) x+x2 else break] * operand[y2][x2]
                    }
                    result[y][x] = sum
                }
            }
        }
        return result
    }
    /** deepCopy()
     * ------------
     *  This method copies the MutableList<MutableList<Player?>> method deeply,
     *  meaning all elemenents are copied, not just the top level List
     */
    private fun Array<Array<Player?>>.deepCopy() : Array<Array<Player?>>{
        var new = this.clone()
        for(x in 0 until this.size){
            new[x] = this[x].clone()
            for(y in 0 until this[0].size)
                new[x][y] = this[x][y]?.copy()
        }
        return new
    }
}