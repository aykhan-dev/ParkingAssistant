package ev.aykhan.parkingasistant.viewModel.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import ev.aykhan.parkingasistant.repository.MapRepository

class MapViewModelFactory(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(
            fusedLocationProviderClient,
            MapRepository()
        ) as T
    }

}