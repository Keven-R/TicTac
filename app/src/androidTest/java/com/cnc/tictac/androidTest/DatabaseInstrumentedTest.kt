package com.cnc.tictac.androidTest

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.cnc.tictac.backend.database.*
import com.cnc.tictac.backend.system.HumanPlayer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
/**
 * Instrumented test, which will execute on an Android device.
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {
    private lateinit var playerDAO: PLAYER_DAO
    private lateinit var db: PLAYER_ROOM_DATABASE

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        this.db = Room.databaseBuilder(
            context,
            PLAYER_ROOM_DATABASE::class.java, "database-name"
        )
        .build()
        .also {
            Log.d("<ROOM_TEST>", "Building room database.")
            it.openHelper.writableDatabase.path?.let { it1 -> Log.d("<ROOM_TEST>", it1) }
        }
        this.playerDAO = this.db.getDAO()
    }

    @Test
    fun useAppContext() {
        this.playerDAO.addNewPlayer(100,"Ryan", HumanPlayer(playerName = "Ryan", playerID = 100))

        assert(1 == 1)
    }
}