package com.mindlab.mapboxtest.utils.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.mindlab.mapboxtest.data.IncomingOffer
import com.mindlab.mapboxtest.presentation.LocationTrackingActivity
import com.google.gson.reflect.TypeToken
import com.mindlab.mapboxtest.utils.Constants
import java.lang.reflect.Type


/**
 * Created by Alireza Nezami on 1/12/2022.
 */
const val TAG = "MessagingService"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val data: Map<String, String> = p0.data
        if (data.isNotEmpty()) {
            Log.i(TAG, "onMessageReceived: $data")
            val listType: Type = object : TypeToken<ArrayList<IncomingOffer.Destination>>() {}.type

            val offer = IncomingOffer(
                price = data["price"]?.toLong(),
                origin = Gson()
                    .fromJson(data["origin"],IncomingOffer.Origin::class.java),
                destinations = Gson()
                    .fromJson(data["destinations"],listType),
            )
            val dialogIntent = Intent(this, LocationTrackingActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                val bundle = Bundle().apply {
                    putSerializable(Constants.OFFER_EXTRA, offer)
                }
                putExtras(bundle)
            }
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            application.startActivity(dialogIntent)
        }
    }
}