package com.example.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animations.ui.theme.AnimationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimationApp()
                }
            }
        }
    }
}

@Composable
fun AnimationApp(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)) {
        AnimateAsStateDemo(modifier)
        Spacer(modifier = modifier.height(16.dp))
        CrossFadeDemo(modifier)
        Spacer(modifier = modifier.height(16.dp))
        AnimateVisibilityDemo(modifier)
        Spacer(modifier = modifier.height(16.dp))
        AnimateContentSizeDemo(modifier)
    }
}

@Composable
fun AnimateAsStateDemo(modifier: Modifier) {
    var blue by remember { mutableStateOf(true) }
    val color by animateColorAsState(if (blue) Color.Blue else Color.Green)

    Column() {
        Button(onClick = { blue = !blue }) {
            Text(text = "CHANGE COLOR")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(color = color)
        )
    }
}

private enum class BoxState {
    SMALL,
    LARGE
}

@Composable
fun UpdateTransitionDemo(modifier: Modifier) {
    var boxState by remember { mutableStateOf(BoxState.SMALL) }
    val transition = updateTransition(targetState = boxState, label = "")
    val color by transition.animateColor(label = "") { state ->
        when (state) {
            BoxState.SMALL -> Color.Blue
            BoxState.LARGE -> Color.Magenta
        }
    }
    val size by transition.animateDp(
        label = "",
        transitionSpec = {
            if (targetState == BoxState.LARGE) spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessHigh
                )
            else spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        }
        ) { state ->
        when (state) {
            BoxState.SMALL -> 64.dp
            BoxState.LARGE -> 128.dp
        }
    }

    Column() {
        Button(onClick = {
            boxState = when (boxState) {
                BoxState.SMALL -> BoxState.LARGE
                BoxState.LARGE -> BoxState.SMALL
            }
        }) {
            Text(text = "CHANGE COLOR")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(size)
                .background(color = color)
        )
    }
}

@Composable
fun AnimateVisibilityDemo(modifier: Modifier) {
    var visible by remember { mutableStateOf(true) }

    Column() {
        Button(onClick = { visible = !visible }) {
            Text(text = if (visible) "HIDE" else "SHOW")
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility (visible) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(color = Color.DarkGray)
            )
        }
    }
}

@Composable
fun AnimateContentSizeDemo(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Column() {
        Button(onClick = { expanded = !expanded }) {
            Text(text = if (expanded) "SHRINK" else "EXPAND")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .animateContentSize()
            ) {
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut justo nec felis rutrum posuere. Nulla ac lacinia magna. Cras consequat dictum nulla, ac venenatis eros consequat id. Vestibulum vel dapibus lorem. Sed tristique, turpis eu euismod mattis, odio libero tempor metus, a fermentum eros risus ut velit. Nulla facilisi. Suspendisse potenti. Integer aliquam ultrices lorem, sed vestibulum arcu pulvinar sit amet. Vivamus eleifend auctor tortor, eget lacinia erat sagittis eget. Sed commodo elit nec sapien scelerisque sodales. Curabitur convallis vestibulum neque, eu tempus lectus lacinia vel. In sollicitudin varius arcu, eu facilisis lectus gravida ut. Aliquam erat volutpat. Nullam vulputate nunc id magna fermentum, eget convallis arcu lacinia. Curabitur nec odio vitae tellus tincidunt convallis.",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                color = Color.Black,
                modifier = Modifier.padding(16.dp),
                maxLines = if (expanded) Int.MAX_VALUE else 2
            )
        }
    }
}

private enum class DemoScene {
    Text, Icon
}

@Composable
fun CrossFadeDemo(modifier: Modifier) {
    var scene by remember { mutableStateOf(DemoScene.Text) }

    Column() {
        Button(onClick = {
            scene = when (scene) {
                DemoScene.Text -> DemoScene.Icon
                DemoScene.Icon -> DemoScene.Text
            }
        }) {
            Text(text = "TOGGLE")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Crossfade(label = "", targetState = scene) { scene ->
            when (scene) {
                DemoScene.Text -> Text(
                    text = "Phone",
                    fontSize = 32.sp
                )
                DemoScene.Icon -> Icon(
                    painter = painterResource(id = R.drawable.baseline_local_phone_24),
                    contentDescription = "Phone",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimationAppPreview() {
    AnimationsTheme {
        AnimationApp()
    }
}