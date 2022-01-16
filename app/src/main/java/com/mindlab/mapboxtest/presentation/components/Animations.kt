package com.mindlab.mapboxtest.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.hypot

/**
 * Created by Alireza Nezami on 1/14/2022.
 */
@Composable
fun CircularRevealLayout(
    modifier: Modifier = Modifier,
    toggled: Boolean = true,
    onFinish: () -> Unit
) {

    var radius by remember { mutableStateOf(0f) }
    var viewHeight by remember { mutableStateOf(0f) }
    var viewWidth by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier

            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = if (toggled) Color.Transparent else Color.Black.copy(0.5f),
                    radius = radius,
                    center = Offset(size.width / 2, size.height / 2).also {
                        viewHeight = size.height / 2
                        viewWidth = size.width / 2
                    },
                )
            },
    )
    val animatedRadius = remember { Animatable(0f) }
    val (width, height) = with(LocalConfiguration.current) {
        with(LocalDensity.current) { screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx() }
    }
    val maxRadiusPx = hypot(viewWidth, viewHeight)

    LaunchedEffect(toggled) {
        if (!toggled) {
            animatedRadius.animateTo(
                maxRadiusPx, animationSpec = tween(
                    durationMillis = 3000
                )
            ) {
                radius = value
            }
            // reset the initial value after finishing animation
            animatedRadius.snapTo(0f)
            onFinish()
        } else {
            animatedRadius.snapTo(0f)
            radius = 0.0f
        }
    }
}


