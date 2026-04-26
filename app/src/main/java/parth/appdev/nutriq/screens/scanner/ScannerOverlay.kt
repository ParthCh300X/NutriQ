package parth.appdev.nutriq.presentation.screens.scanner

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScannerOverlay() {

    Box(modifier = Modifier.fillMaxSize()) {

        // 🌑 Full dark layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        // 🔳 Transparent scan box
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.Center)
        ) {

            // Border frame
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .border(2.dp, Color.White)
            )

            // 🔥 Animated scan line
            ScanLine()
        }
    }
}
@Composable
fun ScanLine() {

    val infiniteTransition = rememberInfiniteTransition()

    val yOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 260f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .offset(y = yOffset.dp)
            .background(Color.Green)
    )
}