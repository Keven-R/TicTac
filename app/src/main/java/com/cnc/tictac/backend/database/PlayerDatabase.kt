package com.cnc.tictac.backend.database
/** This is a ROOM database that handles storing the player objects,
 *     storing the wins, losses, and draws,
 *     and generating unique IDs for each player.
 */
import android.content.Context
import androidx.room.*
import com.cnc.tictac.backend.system.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
/**
 *
 */
class Converters {
    @TypeConverter
    fun toPlayer(json: String): HumanPlayer {
        val obj = Json.decodeFromString<HumanPlayer>(json)
        return obj
    }
    @TypeConverter
    fun fromPlayer(player: HumanPlayer): String {
        val json = Json.encodeToString(player)
        return json
    }
}
/** This generates the table for player entry */
const val TABLE_NAME = "PLAYER_TABLE"
//constructing entity ->https://developer.android.com/training/data-storage/room/defining-data
@Entity(tableName = TABLE_NAME)
class PLAYER_ENTRY(
    @PrimaryKey val id: Int, //autogenerate unique Keys
    val name    : String?,
    val obj     : HumanPlayer?,
    val wins    : Int,
    val losses  : Int,
    val draws   : Int,
    )
/** This is a data access object for the player database **/
//https://developer.android.com/training/data-storage/room/accessing-data
@Dao
interface PLAYER_DAO {
    /** Getters **/
    @Query("SELECT obj FROM $TABLE_NAME")
    fun getAllPlayers() : List<HumanPlayer?>
    @Query("SELECT obj FROM $TABLE_NAME WHERE id = :searchID")
    fun selectPlayerbyID(searchID : Int?): HumanPlayer?
    // get wins, losses, draws
    @Query("SELECT wins FROM $TABLE_NAME WHERE id = :playerID")
    fun getWins(playerID : Int?) : Int
    @Query("SELECT losses FROM $TABLE_NAME WHERE id = :playerID")
    fun getLosses(playerID : Int?) : Int
    @Query("SELECT draws FROM $TABLE_NAME WHERE id = :playerID")
    fun getDraws(playerID : Int?) : Int

    /** Insertion **/
    @Query("INSERT INTO $TABLE_NAME (id, name, obj, wins, losses, draws) " +
            "VALUES (:playerID, :playerName, :player, 0, 0, 0)")
    fun addNewPlayer(playerID : Int?, playerName : String, player : HumanPlayer)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :playerID ")
    fun removePlayer(playerID : Int?)

    /** Deletion **/
    @Query("DELETE FROM $TABLE_NAME WHERE obj = :player")
    fun removePlayer(player : HumanPlayer)

    /** Increment wins, losses, draws **/
    @Query("UPDATE $TABLE_NAME SET wins = :newWins WHERE obj = :player")
    fun updateWins(newWins : Int, player : HumanPlayer)
    @Query("UPDATE $TABLE_NAME SET losses = :newLosses WHERE obj = :player")
    fun updateLosses(newLosses : Int, player : HumanPlayer)
    @Query("UPDATE $TABLE_NAME SET draws = :newDraws WHERE obj = :player")
    fun updateDraws(newDraws : Int, player : HumanPlayer)

}
/** This is the database class for the players **/
// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [PLAYER_ENTRY::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
public abstract class PLAYER_ROOM_DATABASE : RoomDatabase() {
    abstract fun getDAO(): PLAYER_DAO
    companion object {
        @Volatile
        private var Instance: PLAYER_ROOM_DATABASE? = null
        fun getDatabase(context: Context): PLAYER_ROOM_DATABASE {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PLAYER_ROOM_DATABASE::class.java, "PLAYER_ROOM_DATABASE_imp")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}


