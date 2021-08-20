package ev.aykhan.parkingasistant.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MapPointEntity(
    @PrimaryKey val id: Int = 1,
    val lat: Double,
    val lng: Double,
)