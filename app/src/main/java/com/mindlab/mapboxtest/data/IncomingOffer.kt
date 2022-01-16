package com.mindlab.mapboxtest.data

import kotlinx.serialization.Serializable

@Serializable
data class IncomingOffer(
    val price: Long?,
    val origin: Terminal,
    val destinations: List<Terminal>
) : java.io.Serializable {

    @Serializable
    data class Terminal(
        val text: String,
        val lat: Double,
        val lng: Double
    ) : java.io.Serializable
}