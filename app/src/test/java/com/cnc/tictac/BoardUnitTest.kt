import com.cnc.tictac.backend.system.system.Board
import com.cnc.tictac.backend.system.system.HumanPlayer
import com.cnc.tictac.backend.system.system.Player
import org.junit.Test

/**
 * Unit tests for the Board class
 */
class BoardUnitTest {
    /**
     * Constructor test module
     */
    @Test
    fun constructor_isCorrect() {
        var board : Board = Board()
        // Confirm that board is of the correct dimensions
        assert(board.boardState.size == 3)
        assert(board.boardState[0].size == 3)
        // Confirm that board history is equal to current board state
        assert(board.boardHistory.peek() == board.boardState)
        // Confirm board is initialised to null
        assert(board.boardHistory.peek()[0][0] == null)
    }
    /**
     * placePuck test modules
     */
    @Test
    fun placePuck_isCorrect() {
        var board : Board = Board()
        var player : Player = HumanPlayer()
        board.placePuck(player, 0, 0)
        // Confirm player marker is placed on board
        assert(board.boardState[0][0] == player)
        // Confirm board history is updated with event
        assert(board.boardHistory.peek()[0][0] == player)
    }
    @Test
    fun placePuck_errorHandling() {
        var board : Board = Board()
        var player : Player = HumanPlayer()
        assert(!board.placePuck(player, 100, 100))
    }
    /**
     * undoPreviousMove test modules
     */
    @Test
    fun undoPreviousMove_isCorrect(){
        var board : Board = Board()
        var player : Player = HumanPlayer()

        board.placePuck(player, 0, 0)
        board.placePuck(player, 1, 1)
        var boardState1 = board.boardHistory.peek() // History of board before puck placement

        board.placePuck(player, 2, 0)
        var boardState2 = board.boardHistory.peek() // History of board after pluck placement

        board.undoPreviousMove()
        var boardState3 = board.boardHistory.peek() // History of board after undo-ing previous move

        println(board.toString())

        /*
        assert(!boardState1.contentEquals(boardState2))
        assert(boardState1.contentEquals(boardState3))
        assert(!boardState2.contentEquals(boardState3))
        */

    }
}