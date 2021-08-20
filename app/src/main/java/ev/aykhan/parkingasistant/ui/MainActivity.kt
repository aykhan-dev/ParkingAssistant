package ev.aykhan.parkingasistant.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ev.aykhan.parkingasistant.manager.PreferenceManager
import ev.aykhan.parkingasistant.navigation.InAppRoutes
import ev.aykhan.parkingasistant.ui.screens.IntroScreen
import ev.aykhan.parkingasistant.ui.screens.MapScreen
import ev.aykhan.parkingasistant.ui.theme.ParkingAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ParkingAssistantTheme {

                Surface(color = MaterialTheme.colors.background) {
                    ParkingAssistantApp()
                }

            }

        }

    }

}

@Composable
fun ParkingAssistantApp() {

    val systemUiController = rememberSystemUiController()
    val isLightTheme = MaterialTheme.colors.isLight

    val navController = rememberNavController()

    SideEffect {

        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = isLightTheme
        )

    }

    NavHost(
        navController = navController,
        startDestination = if (PreferenceManager.isFirstLaunch) InAppRoutes.INTRO_SCREEN else InAppRoutes.MAP_SCREEN
    ) {
        composable(InAppRoutes.INTRO_SCREEN) { IntroScreen(navController) }
        composable(InAppRoutes.MAP_SCREEN) { MapScreen(navController) }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ParkingAssistantTheme {
        ParkingAssistantApp()
    }
}