package com.mindlab.mapboxtest

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by Alireza Nezami on 1/12/2022.
 */
const val TAG = "mapboxtest"
class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()
        getFirebaseToke()
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
}