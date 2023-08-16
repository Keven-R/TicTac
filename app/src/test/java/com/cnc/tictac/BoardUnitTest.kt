import com.cnc.tictac.system.Board
import com.cnc.tictac.system.HumanPlayer
import com.cnc.tictac.system.Player
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
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
        assertEquals(board.getBoardState().size, 3)
        assertEquals(board.getBoardState()[0].size, 3)
        // Confirm board is initialised to null
        assertEquals(board.getBoardHistory().last()[0][0], null)
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
        assertEquals(board.getBoardState()[0][0]?.playerID, player.playerID)
        // Confirm board history is updated with event
        assertEquals(board.getBoardHistory().last()[0][0]?.playerID, player.playerID)
    }
    @Test
    fun placePuck_errorHandling() {
        var board : Board = Board()
        var player : Player = HumanPlayer()
        assertTrue(!board.placePuck(player, 100, 100))
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
        var boardState1 = board.getBoardHistory().last() // History of board before puck placement
        board.placePuck(player, 2, 0)
        var boardState2 = board.getBoardHistory().last() // History of board after pluck placement
        board.undoPreviousMove()
        var boardState3 = board.getBoardHistory().last() // History of board after undo-ing previous move
        assertNotEquals(boardState1, boardState2)
        assertEquals(boardState1, boardState3)
        assertNotEquals(boardState2, boardState3)
    }
    /**
     * clearGameBoard test modules
     */
    @Test
    fun clearGameBoard_isCorrect(){
        var board : Board = Board()
        var player : Player = HumanPlayer()
        board.placePuck(player, 0, 0)
        board.placePuck(player, 1, 1)
        board.clearGameBoard()
        assertEquals(board.getBoardState()[0][0], null)
    }
    /**
     * searchWinCondition test modules
     */
    @Test
    fun searchWinCondition_isCorrect(){
        var board : Board = Board(width = 6, height = 6, minimumWin = 4)
        var player : Player = HumanPlayer()
        board.placePuck(player, 2, 0)
        board.placePuck(player, 0, 2)
        //board.placePuck(player, 0, 2)
        board.placePuck(player, 3, 0)
        board.placePuck(player, 4, 0)
        board.placePuck(player, 5, 0)

        board.placePuck(player, 4, 1)
        board.placePuck(player, 5, 2)

        board.placePuck(player, 2, 2)
        board.placePuck(player, 3, 3)
        board.placePuck(player, 4, 4)
        board.placePuck(player, 5, 5)
        //board.placePuck(player, 2, 0)

        assertEquals(board.searchWinCondition(player).second, true)
    }
}