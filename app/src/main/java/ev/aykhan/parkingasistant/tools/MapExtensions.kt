package ev.aykhan.parkingasistant.tools

import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.LatLng

fun GoogleMap.zoomTo(coordinates: LatLng?) {
    coordinates?.let { latLng ->
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
        this.animateCamera(cameraUpdate)
    }
}