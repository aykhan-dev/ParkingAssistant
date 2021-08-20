package ev.aykhan.parkingasistant.viewModel.map

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import ev.aykhan.parkingasistant.enums.ParkingState
import ev.aykhan.parkingasistant.model.MapPoint
import ev.aykhan.parkingasistant.repository.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MapViewModel(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val mapRepository: MapRepository,
) : ViewModel() {

    private val _parkingState = MutableLiveData(ParkingState.NOT_PARKED)
    val parkingState: LiveData<ParkingState?> get() = _parkingState

    private val _lastKnownLocation = MutableLiveData<MapPoint?>(null)
    val lastKnownLocation: LiveData<MapPoint?> get() = _lastKnownLocation

    val carLocation: LiveData<MapPoint?> = mapRepository.carLocation.map {
        _parkingState.value = if (it == null) ParkingState.NOT_PARKED else ParkingState.PARKED
        return@map it?.let { MapPoint(latLng = LatLng(it.lat, it.lng)) }
    }

    private var _marker: Marker? = null

    fun requestForLastKnownLocation() {

        viewModelScope.launch(Dispatchers.IO) {
            val coordinates = getCurrentLocation()
            _lastKnownLocation.postValue(MapPoint(latLng = coordinates))
        }

    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation() = suspendCoroutine<LatLng> { continuation ->

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                it?.let { location ->
                    continuation.resume(LatLng(location.latitude, location.longitude))
                } ?: continuation.resumeWithException(Throwable("Not found"))
            }
            .addOnFailureListener(continuation::resumeWithException)
            .addOnCanceledListener {
                continuation.resumeWithException(Throwable("Cancelled"))
            }

    }

    fun savePlaceOfCar() {

        viewModelScope.launch(Dispatchers.IO) {
            val coordinates = getCurrentLocation()
            mapRepository.saveCarParkingSpot(
                MapPoint(
                    latLng = LatLng(
                        coordinates.latitude,
                        coordinates.longitude
                    )
                )
            )
        }

    }

    fun foundCar() {

        viewModelScope.launch(Dispatchers.IO) {
            mapRepository.clearCarParkingSpot()
        }

    }

    fun saveMarker(marker: Marker) {
        clearMarker()
        _marker = marker
    }

    fun clearMarker() {
        _marker?.remove()
    }

}