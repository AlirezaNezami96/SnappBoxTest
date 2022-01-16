package com.mindlab.mapboxtest.utils

import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService

/**
 * Created by Alireza Nezami on 1/16/2022.
 */
object DeviceUtil {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun isDeviceLocked(context: Context): Boolean {
        val keyguardManager: KeyguardManager? = context.getSystemService()
        (Context.KEYGUARD_SERVICE)
        keyguardManager?.let {
            return keyguardManager.isDeviceLocked
        }
        return false
    }
}