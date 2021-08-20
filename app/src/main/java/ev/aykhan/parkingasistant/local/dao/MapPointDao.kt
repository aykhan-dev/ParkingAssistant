package ev.aykhan.parkingasistant.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ev.aykhan.parkingasistant.local.entities.MapPointEntity

@Dao
interface MapPointDao {

    @Query("SELECT * FROM mappointentity WHERE id == 1")
    fun getCarParkingSpot(): LiveData<MapPointEntity?>

    @Query("DELETE FROM mappointentity WHERE id == 1")
    suspend fun clearCarParkingSpot()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCarParkingSpot(mapPointEntity: MapPointEntity)

}