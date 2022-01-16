package com.mindlab.mapboxtest.utils

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.service.notification.Condition.SCHEME
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


/**
 * Created by Alireza Nezami on 1/16/2022.
 */
object PermissionUtil {

    @RequiresApi(Build.VERSION_CODES.M)
    fun goToNotificationSettings(activity: Activity) {
        if (!isLockScreenPermissionGranted(activity)) {
            val intent = Intent()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts(SCHEME, activity.packageName, null)
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", activity.packageName)
            } else
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", activity.packageName)
            intent.putExtra("app_uid", activity.applicationInfo.uid)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            try {
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun goToAppOverlaySettings(context: Activity) {
        if (!isAppOverlayPermissionGranted(context)) {
            val intent =
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
            context.startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isLockScreenPermissionGranted(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isAppOverlayPermissionGranted(activity: Activity): Boolean {
        return Settings.canDrawOverlays(activity)
    }
}