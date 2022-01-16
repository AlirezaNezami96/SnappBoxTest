package com.mindlab.mapboxtest

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mindlab.mapboxtest.utils.Constants

/**
 * Created by Alireza Nezami on 1/12/2022.
 */
const val TAG = "mapboxtest"
class MyApp:Application() {


    override fun onCreate() {
        super.onCreate()
        getFirebaseToke()
        createNotificationChannel()
    }

    private fun getFirebaseToke() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result

            val msg = getString(R.string.fcm_token, token)
            Log.d(TAG, msg)
        })
    }

    private fun createNotificationChannel() {
        val notificationManager = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        notificationManager.createNotificationChannel(channel)
    }
}