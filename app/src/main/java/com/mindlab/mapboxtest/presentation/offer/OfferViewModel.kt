package com.mindlab.mapboxtest.presentation.offer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.dsl.cameraOptions
import com.mindlab.mapboxtest.data.IncomingOffer
import com.mindlab.mapboxtest.utils.Constants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by Alireza Nezami on 1/13/2022.
 */
class OfferViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    val offerFlow: IncomingOffer? = state.get(Constants.OFFER_EXTRA) as IncomingOffer?

    private val _events = Channel<OfferEvents>(Channel.UNLIMITED)
    val events =
        _events.consumeAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private val _onFinish = MutableStateFlow(false)
     val finished = _onFinish.asSharedFlow()

    fun flyToMarker(terminal: IncomingOffer.Terminal) {
        viewModelScope.launch {
            _events.send(
                OfferEvents.ChangeCameraToPosition(
                    cameraOptions {
                        center(
                            Point.fromLngLat(
                                terminal.lng,
                                terminal.lat,
                            )
                        )
                        zoom(16.0)
                        bearing(180.0)
                        pitch(50.0)
                    },
                )
            )
        }
    }

    fun onFinish() {
        _onFinish.value = true
    }
}

sealed class OfferEvents() {
    data class ChangeCameraToPosition(val option: CameraOptions) : OfferEvents()
}
