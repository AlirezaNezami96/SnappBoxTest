package com.mindlab.mapboxtest.presentation.offer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mindlab.mapboxtest.R
import com.mindlab.mapboxtest.data.IncomingOffer
import com.mindlab.mapboxtest.presentation.components.ButtonTitle
import com.mindlab.mapboxtest.presentation.components.CircularRevealLayout
import com.mindlab.mapboxtest.presentation.components.HeaderTitle
import com.mindlab.mapboxtest.presentation.components.SubtitleTitle
import com.mindlab.mapboxtest.presentation.theme.ButtonColor
import com.mindlab.mapboxtest.presentation.theme.PrimaryColor
import com.mindlab.mapboxtest.presentation.theme.ProgressColor
import com.mindlab.mapboxtest.utils.Extensions.toPersianNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

/**
 * Created by Alireza Nezami on 1/13/2022.
 */
@Composable
fun OfferViews(
    modifier: Modifier = Modifier,
    viewModel: OfferViewModel,
    mapView: MapView,
    offer: IncomingOffer
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is OfferEvents.ChangeCameraToPosition -> {
                    mapView.getMapboxMap().flyTo(
                        cameraOptions = event.option,
                        mapAnimationOptions {
                            duration(3000)
                        }
                    )
                }

            }
        }
    }


    Box(modifier = modifier) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                mapView
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(top = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {

                    Spacer(
                        modifier = Modifier
                            .height(64.dp)
                            .background(color = Color.Gray)
                    )

                    SubtitleTitle(
                        modifier = Modifier
                            .clickable {
                                viewModel.flyToMarker(offer.origin)
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        text = " مبدا: ${offer.origin.text}",
                        color = Color.Black
                    )

                    offer.destinations.forEachIndexed { index, terminal ->
                        SubtitleTitle(
                            modifier = Modifier
                                .clickable {
                                    viewModel.flyToMarker(offer.destinations[index])
                                }
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            text = " مقصد: ${terminal.text}",
                            color = Color.Black
                        )
                    }

                    ActionButton(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        onTimeOut = {
                            viewModel.onFinish()
                        }
                    )
                }
            }

            HeaderTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        color = PrimaryColor,
                        shape = MaterialTheme.shapes.large
                    ),
                text = "${offer.price.toPersianNumber()} تومان",
                color = Color.White
            )

        }

    }

}

@Composable
fun ActionButton(
    modifier: Modifier,
    onTimeOut: () -> Unit
) {
    val totalTime = 30.00f
    var progressValue by remember {
        mutableStateOf(1.00f)
    }
    var currentTime by remember {
        mutableStateOf(1.00f)
    }
    var isTimerRunning by remember {
        mutableStateOf(true)
    }

    var isToggled by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime <= totalTime && isTimerRunning) {
            delay(50L)
            currentTime += 0.05f
            progressValue = (currentTime / totalTime)
            Log.i("TAG", "ActionButton: $progressValue")
        } else {
            isTimerRunning = false
            onTimeOut()
        }

    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        isToggled = false
        DisposableEffect(Unit) {
            onDispose {
                println("released")
                isToggled = true
            }
        }
    }

    Box(
        modifier = modifier
            .background(
                color = ButtonColor,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            )
    ) {

        CircularRevealLayout(
            modifier = Modifier
                .matchParentSize(),
            isToggled,
            onFinish = onTimeOut
        )

        LinearProgressIndicator(
            progress = progressValue,
            modifier = Modifier
                .matchParentSize()
                .clip(
                    shape = MaterialTheme.shapes.medium
                ),
            color = ProgressColor,
            backgroundColor = Color.Transparent
        )

        ButtonTitle(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    val size = coordinates.size.toSize()
                    Log.i("TAG", "onGloballyPositioned: ${size.width}")
                }
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = stringResource(R.string.accept_offer),
            color = Color.White
        )


    }
}