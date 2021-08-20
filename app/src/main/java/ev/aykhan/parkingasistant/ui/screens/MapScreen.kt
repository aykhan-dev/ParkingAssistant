package ev.aykhan.parkingasistant.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import ev.aykhan.parkingasistant.R
import ev.aykhan.parkingasistant.enums.ParkingState
import ev.aykhan.parkingasistant.model.MapPoint
import ev.aykhan.parkingasistant.tools.rememberMapViewWithLifecycle
import ev.aykhan.parkingasistant.tools.zoomTo
import ev.aykhan.parkingasistant.ui.reusable.CornFlowerBlueFab
import ev.aykhan.parkingasistant.ui.reusable.FadeInOut
import ev.aykhan.parkingasistant.ui.reusable.ParkingAssistantDefaultButton
import ev.aykhan.parkingasistant.ui.reusable.VerticalSlideFadeInOut
import ev.aykhan.parkingasistant.ui.theme.KellyGreen
import ev.aykhan.parkingasistant.ui.theme.RedOrange
import ev.aykhan.parkingasistant.viewModel.map.MapViewModel
import ev.aykhan.parkingasistant.viewModel.map.MapViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navHostController: NavHostController,
    mapViewModel: MapViewModel = viewModel(
        factory = MapViewModelFactory(LocationServices.getFusedLocationProviderClient(LocalContext.current))
    ),
) {

    val context = LocalContext.current

    val mapPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        PermissionsRequired(
            multiplePermissionsState = mapPermissionsState,
            permissionsNotGrantedContent = {
                MapPermissionsNotGrantedUI(
                    onRequestPermission = {
                        mapPermissionsState.launchMultiplePermissionRequest()
                    },
                    onDenyRequestPermission = {
                        navHostController.popBackStack()
                    }
                )
            },
            permissionsNotAvailableContent = {
                MapPermissionsNotAvailableUI(
                    onOpenSettings = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context.packageName, null)
                        intent.data = uri
                        context.startActivity(intent)
                    }
                )
            },
        ) {
            MapLayer(mapViewModel)
            MapOverlay(mapViewModel)
        }

    }

}

@Composable
private fun MapPermissionsNotGrantedUI(
    onRequestPermission: () -> Unit,
    onDenyRequestPermission: () -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.request_permission),
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            ParkingAssistantDefaultButton(
                onClick = onRequestPermission,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = MaterialTheme.typography.button,
                )
            }
            Spacer(Modifier.width(8.dp))
            ParkingAssistantDefaultButton(
                onClick = onDenyRequestPermission,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.nope),
                    style = MaterialTheme.typography.button,
                )
            }
        }
    }

}

@Composable
private fun MapPermissionsNotAvailableUI(
    onOpenSettings: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.permission_not_available),
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ParkingAssistantDefaultButton(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.open_settings),
                style = MaterialTheme.typography.button,
            )
        }
    }

}

@SuppressLint("MissingPermission")
@Composable
fun MapContainer(
    map: MapView,
    userLocation: MapPoint?,
    carLocation: MapPoint?,
    onPinDropped: (Marker) -> Unit,
    onRemoveMarker: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    val carParkingSpotLabel = stringResource(R.string.car_location)

    AndroidView(factory = { map }) { mapView ->

        coroutineScope.launch {

            val googleMap = mapView.awaitMap()

            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false

            googleMap.zoomTo(userLocation?.latLng)

            carLocation?.let { point ->

                val markerOptions = MarkerOptions().apply {
                    position(point.latLng)
                    title(carParkingSpotLabel)
                }

                val marker = googleMap.addMarker(markerOptions)
                onPinDropped(marker)

            } ?: onRemoveMarker()

        }

    }

}

@Composable
private fun MapLayer(mapViewModel: MapViewModel) {

    val mapView = rememberMapViewWithLifecycle()

    val lastKnownLocation by mapViewModel.lastKnownLocation.observeAsState()
    val carLocation by mapViewModel.carLocation.observeAsState()

    LaunchedEffect(mapView) {
        mapViewModel.requestForLastKnownLocation()
    }

    MapContainer(
        map = mapView,
        userLocation = lastKnownLocation,
        carLocation = carLocation,
        onPinDropped = { marker -> mapViewModel.saveMarker(marker) },
        onRemoveMarker = { mapViewModel.clearMarker() }
    )

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MapOverlay(mapViewModel: MapViewModel) {

    val parkingState by mapViewModel.parkingState.observeAsState()

    val buttonLabel = stringResource(
        when (parkingState) {
            ParkingState.PARKED -> R.string.i_found_car
            else -> R.string.i_parked_car
        }
    )

    Column {

        CarStatusCard(
            parkingState = parkingState,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 23.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp), clip = true)
        )

        CornFlowerBlueFab(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 16.dp),
            onClick = { mapViewModel.requestForLastKnownLocation() },
            iconId = R.drawable.ic_gps_fixed,
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        ParkingAssistantDefaultButton(
            onClick = {
                when (parkingState) {
                    ParkingState.PARKED -> mapViewModel.foundCar()
                    else -> mapViewModel.savePlaceOfCar()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

            FadeInOut(targetState = parkingState) {

                Text(
                    text = buttonLabel,
                    style = MaterialTheme.typography.button,
                )

            }

        }

    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
private fun CarStatusCard(
    parkingState: ParkingState?,
    modifier: Modifier = Modifier,
) {

    var isExpanded by remember { mutableStateOf(true) }

    val titleColor = when (parkingState) {
        ParkingState.PARKED -> KellyGreen
        else -> RedOrange
    }

    val dropDownRotation by animateFloatAsState(if (isExpanded) 0f else 180f)

    val title = stringResource(
        when (parkingState) {
            ParkingState.PARKED -> R.string.you_parked
            else -> R.string.not_parked_yet
        }
    )

    val description = stringResource(
        when (parkingState) {
            ParkingState.PARKED -> R.string.you_parked_description
            else -> R.string.not_parked_yet_description
        }
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        onClick = { isExpanded = !isExpanded },
        modifier = modifier,
    ) {

        Column(
            modifier = Modifier
                .padding(24.dp)
                .wrapContentSize()
        ) {

            Row {

                VerticalSlideFadeInOut(
                    targetState = parkingState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {

                    Text(
                        text = title,
                        color = titleColor,
                        style = MaterialTheme.typography.h1,
                    )

                }

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.rotate(dropDownRotation),
                )

            }

            AnimatedVisibility(visible = isExpanded) {

                VerticalSlideFadeInOut(targetState = parkingState) {

                    Text(
                        text = description,
                        style = MaterialTheme.typography.caption,
                    )

                }

            }

        }

    }

}