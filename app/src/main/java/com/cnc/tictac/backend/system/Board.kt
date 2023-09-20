package com.cnc.tictac.backend.system
import android.util.Log
import java.util.Arrays
import java.util.Stack
/**
 * Please See BACKEND_README.md on GITHUB for implementation information.
 */
enum class WinCondition{
    VERTICAL,
    HORISONTAL,
    DIAGONAL_1,
    DIAGONAL_2,
    NO_WIN,
    DRAW
}
private const val TAG = "Board"
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
    fun getMinimumWin() : Int {
        return this.minimumWin
    }
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
        return try {
            if(this.boardState[y][x] == null){
                this.boardState[y][x] = currentPlayer
                boardHistory.push(this.boardState.deepCopy())
                this.movesMade ++
                true
            } else {
                false
            }
        } catch (e : Exception){
            false
        }
    }
    /** undoPreviousMove (void)
     * -------------------------
     * This method pops the boardHistory (removing the latest entry),
     * Then sets the boardstate to the top of the boardHistory stack.
     */
    fun undoPreviousMove(){
        if(this.movesMade != 0) {
            this.movesMade--
            this.boardHistory.pop()
            this.boardState = this.boardHistory.peek()
        }
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
        this.movesMade = 0
    }
    /** searchWinCondition()
     * ----------------------
     *  Performs convolutions to search for win conditions on the board
     *  Modified: 23/08... Allows searching for DRAWS now. Modified output to be an enum, reduces
     *  errors associated with using strings.
     */
    fun getWinCoordinates(player : Player, winCondition : WinCondition) : Pair<Pair<Int, Int>, Pair<Int, Int>>? {
        Log.d(TAG, "Searching win coordinates")
        val positionMap = generatePositionMap(player)
        positionMap.print2D()
        // Convolutional templates for calculation.
        val convHorisontal = Array(this.minimumWin, { Array(1, { 1 }) })
        val convVertical = Array(1, { Array(this.minimumWin, { 1 }) })
        val convDiagonal1 = Array(this.minimumWin, { index1 -> Array(this.minimumWin, { index2 -> if (index1 == index2) { 1 } else { 0 } }) })
        val convDiagonal2 = Array(this.minimumWin, { index1 -> Array(this.minimumWin, { index2 -> if ((this.minimumWin - 1 - index1) == index2) { 1 } else { 0 } }) })

        var minorIndex: Pair<Int, Int>
        var majorIndex: Pair<Int, Int>
        when (winCondition) {
            WinCondition.VERTICAL -> {
                val convolutionVertical = positionMap.convolution2D(convVertical)
                convolutionVertical.print2D()
                val index = findIndexGEThan(convolutionVertical, this.minimumWin)
                minorIndex = Pair(index!!.first, index!!.second)
                majorIndex = Pair(index!!.first, index!!.second + this.minimumWin-1)
            }
            WinCondition.HORISONTAL -> {
                val convolutionHorisontal = positionMap.convolution2D(convHorisontal)
                convolutionHorisontal.print2D()
                val index = findIndexGEThan(convolutionHorisontal, this.minimumWin)
                minorIndex = Pair(index!!.first, index!!.second)
                majorIndex = Pair(index!!.first + this.minimumWin-1, index!!.second)
            }
            WinCondition.DIAGONAL_1 -> {
                val convolutionDiagonal1 = positionMap.convolution2D(convDiagonal1)
                convolutionDiagonal1.print2D()
                val index = findIndexGEThan(convolutionDiagonal1, this.minimumWin)
                minorIndex = Pair(index!!.first, index!!.second)
                majorIndex = Pair(index!!.first + this.minimumWin-1, index!!.second + this.minimumWin-1)
            }
            WinCondition.DIAGONAL_2 -> {
                val convolutionDiagonal2 = positionMap.convolution2D(convDiagonal2)
                convolutionDiagonal2.print2D()
                val index = findIndexGEThan(convolutionDiagonal2, this.minimumWin)
                minorIndex = Pair(index!!.first, index!!.second + this.minimumWin-1)
                majorIndex = Pair(index!!.first + this.minimumWin-1, index!!.second)
            }
            WinCondition.NO_WIN -> return null
            WinCondition.DRAW -> return null
        }
        Log.d(TAG, "minor index found at ${minorIndex?.first}, ${minorIndex?.second}")
        Log.d(TAG, "major index found at ${majorIndex?.first}, ${majorIndex?.second}")
        return Pair(minorIndex, majorIndex)
    }
    fun searchWinCondition(player : Player) : WinCondition{
        Log.d(TAG, "Searching Win Condition")
        val positionMap = generatePositionMap(player)
        positionMap.print2D()
        // Convolutional templates for calculation.
        val convHorisontal          = Array(this.minimumWin, { Array(1, {1}) })
        val convVertical            = Array(1, { Array(this.minimumWin, {1}) })
        val convDiagonal1           = Array(this.minimumWin, {index1 -> Array(this.minimumWin, {index2 -> if(index1 == index2){ 1 } else{ 0 } })})
        val convDiagonal2           = Array(this.minimumWin, {index1 -> Array(this.minimumWin, {index2 -> if((this.minimumWin-1 - index1) == index2){ 1 } else{ 0 } })})
        // Performing convolution.
        val convolutionVertical     = positionMap.convolution2D(convVertical)
        val convolutionHorisontal   = positionMap.convolution2D(convHorisontal)
        val convolutionDiagonal1    = positionMap.convolution2D(convDiagonal1)
        val convolutionDiagonal2    = positionMap.convolution2D(convDiagonal2)
        // Checking to see if a win exists.
        val vertMatch = Arrays.stream(convolutionVertical).anyMatch      { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
        val horiMatch = Arrays.stream(convolutionHorisontal).anyMatch    { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
        val dia1Match = Arrays.stream(convolutionDiagonal1).anyMatch     { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
        val dia2Match = Arrays.stream(convolutionDiagonal2).anyMatch     { a -> Arrays.stream(a).anyMatch { n -> n == this.minimumWin} }
        // Win return logic
        return if(vertMatch){
            Log.d(TAG, "Vertical win condition found.")
            WinCondition.VERTICAL
        } else if (horiMatch){
            Log.d(TAG, "Horisontal win condition found")
            WinCondition.HORISONTAL
        } else if (dia1Match){
            Log.d(TAG, "Diagonal 1 win condition found")
            WinCondition.DIAGONAL_1
        } else if (dia2Match){
            Log.d(TAG, "Diagonal 2 win condition found")
            WinCondition.DIAGONAL_2
        } else if(this.movesMade == this.width * this.height){
            Log.d(TAG, "Draw found.")
            WinCondition.DRAW
        } else {
            Log.d(TAG, "No win condition found.")
            WinCondition.NO_WIN
        }
    }
    private fun generatePositionMap(currentPlayer : Player) : Array<Array<Int>> {
        Log.d(TAG, "Generating position map for player ${currentPlayer.playerID}")
        val positionMap = Array(this.boardState.size, { Array<Int>(this.boardState[0].size, { 0 }) })
        for(y in 0 until this.boardState.size)
            for(x in 0 until this.boardState[0].size)
                positionMap[y][x] = if(this.boardState[x][y] == currentPlayer) {1} else {0}
        return positionMap
    }
    private fun findIndexGEThan(arr : Array<Array<Int>>, item : Int) : Pair<Int, Int>? {
        for (i in arr.indices) {
            for(j in arr[0].indices) {
                if (arr[i][j] >= item) {
                    return Pair(i, j)
                }
            }
        }
        return null
    }
    private fun Array<Array<Int>>.print2D(){
        for (i in this.indices) {
            Log.v(TAG, "Array: ${this[i].contentToString()}")
        }
        Log.v(TAG, "")
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