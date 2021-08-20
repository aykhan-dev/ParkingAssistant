package ev.aykhan.parkingasistant.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ev.aykhan.parkingasistant.local.dao.MapPointDao
import ev.aykhan.parkingasistant.local.entities.MapPointEntity

@Database(entities = [MapPointEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val mapPointDao: MapPointDao
}

object RoomManager {

    private lateinit var _db: AppDatabase
    val db: AppDatabase get() = _db

    fun init(context: Context) {
        if (!::_db.isInitialized) {
            _db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-name"
            ).build()
        }
    }

}