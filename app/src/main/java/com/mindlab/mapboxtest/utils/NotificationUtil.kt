package com.mindlab.mapboxtest.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.mindlab.mapboxtest.R
import com.mindlab.mapboxtest.presentation.offer.OfferActivity

/**
 * Created by Alireza Nezami on 1/16/2022.
 */
object NotificationUtil {

    fun createNotification(context: Context,bundle: Bundle): Notification {
        val contentIntent = Intent(context, OfferActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0)

        val fullScreenIntent = Intent(context, OfferActivity::class.java).apply {
            putExtras(bundle)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, 0)

        return NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(ResourcesCompat.getColor(context.resources, R.color.purple_200, null))
            .setContentTitle(context.getString(R.string.new_offer))
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
    }
}