import com.cnc.tictac.system.Board
import com.cnc.tictac.system.HumanPlayer
import com.cnc.tictac.system.Player
import org.junit.Test

/**
 * Unit tests for the Board class
 */
class BoardUnitTest {
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
        board.placePuck(player, 100, 100)

    }
}