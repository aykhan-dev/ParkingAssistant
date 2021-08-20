package ev.aykhan.parkingasistant.repository

import ev.aykhan.parkingasistant.local.RoomManager
import ev.aykhan.parkingasistant.local.entities.MapPointEntity
import ev.aykhan.parkingasistant.model.MapPoint

class MapRepository {

    private val dao = RoomManager.db.mapPointDao

    val carLocation = dao.getCarParkingSpot()

    suspend fun saveCarParkingSpot(mapPoint: MapPoint) {
        dao.saveCarParkingSpot(
            MapPointEntity(
                id = 1,
                lat = mapPoint.latLng.latitude,
                lng = mapPoint.latLng.longitude,
            )
        )
    }

    suspend fun clearCarParkingSpot() {
        dao.clearCarParkingSpot()
    }

}