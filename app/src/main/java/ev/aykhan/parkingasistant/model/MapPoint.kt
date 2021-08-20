package ev.aykhan.parkingasistant.model

import com.google.android.libraries.maps.model.LatLng
import kotlin.random.Random

data class MapPoint(
    private val id: Int = Random.nextInt(),
    val latLng: LatLng
)