package com.mindlab.mapboxtest.utils.service

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mindlab.mapboxtest.data.IncomingOffer
import com.mindlab.mapboxtest.presentation.offer.OfferActivity
import com.mindlab.mapboxtest.utils.Constants
import com.mindlab.mapboxtest.utils.DeviceUtil
import com.mindlab.mapboxtest.utils.NotificationUtil
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
            val listType: Type = object : TypeToken<ArrayList<IncomingOffer.Terminal>>() {}.type

            val offer = IncomingOffer(
                price = data["price"]?.toLong(),
                origin = Gson()
                    .fromJson(data["origin"], IncomingOffer.Terminal::class.java),
                destinations = Gson()
                    .fromJson(data["destinations"], listType),
            )

            val bundle = Bundle().apply {
                putSerializable(Constants.OFFER_EXTRA, offer)
            }

            // Check if device is locked,
            // So we decide to show fullScreenIntent to user
            // Or show the activity on foreground when the phone lock is on
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 &&
                DeviceUtil.isDeviceLocked(this)
            ) {
                //The screen is off and we are showing a notification and  fullScreenIntent
                NotificationUtil.createNotification(this,bundle)
            } else {
                // The screen is On
                val dialogIntent = Intent(this, OfferActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtras(bundle)
                }
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                application.startActivity(dialogIntent)
            }
        }
    }
}