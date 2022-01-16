package com.mindlab.mapboxtest.presentation.offer

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mindlab.mapboxtest.R
import com.mindlab.mapboxtest.data.IncomingOffer
import com.mindlab.mapboxtest.presentation.base.BaseActivity
import com.mindlab.mapboxtest.presentation.theme.SnappBoxTestTheme
import com.mindlab.mapboxtest.utils.BitmapHelper.bitmapFromDrawableRes
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Alireza Nezami on 1/13/2022.
 */
@InternalCoroutinesApi
val TAG = OfferActivity::class.java.simpleName

class OfferActivity : BaseActivity() {

    private val viewModel: OfferViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = MapView(
            this, MapInitOptions(
                context = this,
                resourceOptions = ResourceOptions.Builder()
                    .accessToken(getString(R.string.public_token)).build()
            )
        )

        lifecycleScope.launchWhenCreated {
            onMapReady()
        }

        observe()
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.finished.collect {
                if (it)
                    finish()
            }
        }
    }

    private fun onMapReady() {
        viewModel.offerFlow?.let { offer ->
            mapView.getMapboxMap().loadStyleUri(
                Style.MAPBOX_STREETS
            ) {
                offer.destinations.forEach {
                    addAnnotationToMap(it)
                }
                addAnnotationToMap(offer.origin)
            }

            val coordinatePints =
                mutableListOf(
                    Point.fromLngLat(offer.origin.lng, offer.origin.lat),
                ).apply {
                    offer.destinations.forEach {
                        add(Point.fromLngLat(it.lng, it.lat))
                    }
                }

            /**
             * Workaround for showing camera bound to location marks
             * Cause the mapbox camera bounding not workin properly
             *
            val coordinates =
            mutableListOf(
            IncomingOffer.Terminal("",offer.origin.lat, offer.origin.lng),
            ).apply {
            offer.destinations.forEach {
            add(IncomingOffer.Terminal("",it.lat, it.lng))
            }
            }

            val  minLng= coordinates.reduce(IncomingOffer.Terminal.Compare::minLng).lng
            val  maxLng=  coordinates.reduce(IncomingOffer.Terminal.Compare::maxLng).lng
            val  minLat= coordinates.reduce(IncomingOffer.Terminal.Compare::minLat).lat
            val maxLat = coordinates.reduce(IncomingOffer.Terminal.Compare::maxLat).lat

            val center = Point.fromLngLat((minLng + maxLng) / 2, (minLat + maxLat) / 2)
            Log.i(TAG, "cameraOptions center is: $center")
            val vInset = (center.latitude() - maxLat).absoluteValue
            val hInset = (center.longitude() - maxLng).absoluteValue
            val edgeInsets = EdgeInsets(vInset, hInset, vInset, hInset)
            Log.i(TAG, "cameraOptions vInset is: $vInset")
            Log.i(TAG, "cameraOptions hInset is: $hInset")


            val cameraOptions = CameraOptions.Builder()
            .center(center)
            .padding(edgeInsets)
            .build()
             */

            val cameraPosition =
                mapView.getMapboxMap()
                    .cameraForCoordinates(
                        coordinatePints,
                    )

            mapView.getMapboxMap().setCamera(cameraPosition)

            setContent {
                SnappBoxTestTheme {
                    Surface() {
                        OfferViews(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = viewModel,
                            mapView = mapView,
                            offer = offer
                        )
                    }
                }
            }
        }
    }

    private fun addAnnotationToMap(terminal: IncomingOffer.Terminal) {
        bitmapFromDrawableRes(
            this@OfferActivity,
            R.drawable.red_marker
        )?.let {
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(terminal.lng, terminal.lat))
                .withIconImage(it)
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

}