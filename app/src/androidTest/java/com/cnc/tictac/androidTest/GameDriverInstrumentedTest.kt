package com.cnc.tictac.androidTest
import android.util.Log
import com.cnc.tictac.backend.gamedriver.GameConfig
import com.cnc.tictac.backend.gamedriver.GameDriver
import com.cnc.tictac.backend.system.HumanPlayer
import com.cnc.tictac.backend.system.WinCondition
import org.junit.Before
import org.junit.Test
class GameDriverInstrumentedTest {
    lateinit var gd : GameDriver
    @Before
    fun before(){
        this.gd = GameDriver(GameConfig())
    }
    @Test
    fun addingAPlayerToTheDatabase() {
        var player1 = HumanPlayer(playerName = "Ryan", playerID = 100)
        var player2 = HumanPlayer(playerName = "Chantelle", playerID = 110)

        this.gd.addPlayerToDatabase(player1)
        this.gd.addPlayerToDatabase(player2)

        var db_Player1 = this.gd.getPlayerFromDatabase(player1.playerID)
        var db_Player2 = this.gd.getPlayerFromDatabase(player2.playerID)

        assert(db_Player2 != null)
        assert(player2.playerName == db_Player2!!.playerName)
        assert(player2.playerID == db_Player2!!.playerID)
        assert(player2.equals(db_Player2))

        assert(db_Player1 != null)
        assert(player1.playerName == db_Player1!!.playerName)
        assert(player1.playerID == db_Player1!!.playerID)
        assert(player1.equals(db_Player1))
    }

    @Test
    fun addingSamePlayerTwice() {
        var player1 = HumanPlayer(playerName = "Ryan", playerID = 100)
        assert(false == this.gd.addPlayerToDatabase(player1))
    }

    @Test
    fun removingPlayers() {
        this.gd.removePlayerFromDatabase(100) // Remove ryan
        this.gd.removePlayerFromDatabase(110) // Remove chantelle
        assert(null == this.gd.getPlayerFromDatabase(100))
        assert(null == this.gd.getPlayerFromDatabase(110))
    }

    @Test
    fun editingPlayerAttribute() {
        var player = HumanPlayer(playerName = "Timmy")
        this.gd.addPlayerToDatabase(player)
        assert(player.playerName == "Timmy")

        this.gd.editPlayerAttribute(player, "playerName", "Johnny")
        var playerDB = this.gd.getPlayerFromDatabase(player.playerID)
        assert(playerDB!!.playerName == "Johnny")

        this.gd.editPlayerAttribute(player, "playerID", 11443322)
        var playerDB2 = this.gd.getPlayerFromDatabase(11443322)
        assert(playerDB!!.playerName == "Johnny")
    }

    @Test
    fun simpleGameTest(){
        /** Creating two players in the database **/
        this.gd = GameDriver(GameConfig(boardHeight = 3, boardWidth = 3, boardMinimumWin = 3))
        this.gd.resetGameDriver()
        var playerRyan = HumanPlayer(playerName = "Ryan")
        var playerChantelle = HumanPlayer(playerName = "Chantelle")
        Log.d("TESTING", playerRyan.toString())
        Log.d("TESTING", playerChantelle.toString())
        this.gd.addPlayerToDatabase(playerRyan)
        this.gd.addPlayerToDatabase(playerChantelle)
        /** Adding players in the database to the game **/
        this.gd.addPlayerToGame(playerRyan)
        this.gd.addPlayerToGame(playerChantelle)
        /** Ryan's move **/
        Log.d("TESTING", "Ryan's move: Current player is " + this.gd.whoIsPlaying().toString())
        assert(this.gd.whoIsPlaying()!!.equals(playerRyan))
        var win = this.gd.playMove(0, 0)
        assert(win == WinCondition.NO_WIN)
        /**
         * X _ _
         * _ _ _
         * _ _ _
         */
        /** Chantelle's move **/
        Log.d("TESTING", "Chantelle's move: Current player is " + this.gd.whoIsPlaying().toString())
        assert(this.gd.whoIsPlaying()!!.equals(playerChantelle))
        win = this.gd.playMove(1, 1)
        assert(win == WinCondition.NO_WIN)
        /**
         * X _ _
         * _ O _
         * _ _ _
         */
        /** Ryan's move **/
        Log.d("TESTING", "Ryan's move: Current player is " + this.gd.whoIsPlaying().toString())
        assert(this.gd.whoIsPlaying()!!.equals(playerRyan))
        win = this.gd.playMove(0, 1)
        assert(win == WinCondition.NO_WIN)
        /**
         * X _ _
         * X O _
         * _ _ _
         */
        /** Chantelle's move **/
        Log.d("TESTING", "Chantelle's move: Current player is " + this.gd.whoIsPlaying().toString())
        assert(this.gd.whoIsPlaying()!!.equals(playerChantelle))
        win = this.gd.playMove(2, 2)
        assert(win == WinCondition.NO_WIN)
        /**
         * X _ _
         * X O _
         * _ _ O
         */
        /** Ryan's move **/
        Log.d("TESTING", "Ryan's move: Current player is " + this.gd.whoIsPlaying().toString())
        assert(this.gd.whoIsPlaying()!!.equals(playerRyan))
        win = this.gd.playMove(0, 2)
        assert(win == WinCondition.WIN)
        /**
         * X _ _
         * X O _
         * X _ O
         */
    }
}