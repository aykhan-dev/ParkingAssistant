package ev.aykhan.parkingasistant.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ev.aykhan.parkingasistant.R
import ev.aykhan.parkingasistant.manager.PreferenceManager
import ev.aykhan.parkingasistant.navigation.InAppRoutes
import ev.aykhan.parkingasistant.ui.reusable.ParkingAssistantDefaultButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun IntroScreen(navController: NavHostController) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(
            top = 39.95.dp,
            start = 37.dp,
            end = 37.dp,
            bottom = 16.dp,
        )
    ) {

        Text(
            text = stringResource(R.string.find_my_car),
            style = MaterialTheme.typography.h1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.intro),
            style = MaterialTheme.typography.caption,
        )

        Spacer(
            modifier = Modifier
                .weight(0.297f)
                .fillMaxSize()
        )

        Image(
            painter = painterResource(R.drawable.vector_intro),
            contentDescription = null
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        ParkingAssistantDefaultButton(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    PreferenceManager.isFirstLaunch = false
                    withContext(Dispatchers.Main) {
                        navController.navigate(InAppRoutes.MAP_SCREEN) {
                            popUpTo(InAppRoutes.INTRO_SCREEN) { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {

            Text(
                text = stringResource(R.string.lets_get_started),
                style = MaterialTheme.typography.button,
            )

        }

    }

}