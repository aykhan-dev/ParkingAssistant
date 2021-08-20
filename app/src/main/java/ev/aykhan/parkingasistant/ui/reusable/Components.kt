package ev.aykhan.parkingasistant.ui.reusable

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ev.aykhan.parkingasistant.ui.theme.CornFlowerBlue

@Composable
fun CornFlowerBlueButton(label: String, onClick: () -> Unit) {

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = CornFlowerBlue),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 37.dp, vertical = 16.dp)
            .requiredHeightIn(min = 52.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.button,
        )
    }

}

@Composable
fun CornFlowerBlueFab(modifier: Modifier = Modifier, onClick: () -> Unit, iconId: Int) {

    FloatingActionButton(
        modifier = modifier,
        backgroundColor = CornFlowerBlue,
        contentColor = Color.White,
        onClick = { onClick() })
    {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "Find me"
        )
    }

}

@Composable
fun ParkingAssistantDefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    block: @Composable () -> Unit,
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = CornFlowerBlue),
        modifier = modifier.requiredHeightIn(min = 52.dp),
    ) {
        block()
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <S> VerticalSlideFadeInOut(
    modifier: Modifier = Modifier,
    clip: Boolean = true,
    targetState: S,
    block: @Composable () -> Unit
) {

    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            (slideInVertically({ a -> a }) + fadeIn() with slideOutVertically(
                { a -> -a }) + fadeOut()).using(SizeTransform(clip = clip))
        },
        modifier = modifier,
    ) {

        block()

    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <S> HorizontalSlideFadeInOut(
    modifier: Modifier = Modifier,
    clip: Boolean = true,
    targetState: S,
    block: @Composable () -> Unit
) {

    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            (slideInHorizontally({ a -> a }) + fadeIn() with slideOutHorizontally(
                { a -> -a }) + fadeOut()).using(SizeTransform(clip = clip))
        },
        modifier = modifier,
    ) {

        block()

    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <S> FadeInOut(
    modifier: Modifier = Modifier,
    clip: Boolean = true,
    targetState: S,
    block: @Composable () -> Unit
) {

    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            (fadeIn() with fadeOut()).using(SizeTransform(clip = clip))
        },
        modifier = modifier,
    ) {

        block()

    }

}