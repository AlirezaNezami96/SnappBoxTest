package com.mindlab.mapboxtest.data

import kotlinx.serialization.Serializable

@Serializable
data class IncomingOffer(
    val price: Long?,
    val origin: Origin,
    val destinations: List<Destination>
) : java.io.Serializable {
    @Serializable
    data class Origin(
        val text: String,
        val lat: Double,
        val lng: Double
    ) : java.io.Serializable

    @Serializable
    data class Destination(
        val text: String,
        val lat: Double,
        val lng: Double
    ) : java.io.Serializable
}