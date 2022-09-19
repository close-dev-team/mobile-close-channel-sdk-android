package com.thecloseapp.closechannelsample

import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.thecloseapp.close.channel.sdk.CloseChannelController
import com.thecloseapp.close.channel.sdk.CloseChannelNotification

class CloseSampleFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "CloseSampleFirebaseMS"
    }

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d(TAG, "Refreshed token: $refreshedToken")

        val permissionGranted = (NotificationManagerCompat.from(application).areNotificationsEnabled())
        val onSuccess = { isPushEnabled: Boolean ->
            Log.v(TAG, "register push - isPushEnabled:${isPushEnabled}")
            Unit
        }

         val closeChannelController = CloseChannelController.getInstance(application)
         closeChannelController.registerPushInfo(refreshedToken, permissionGranted, onSuccess)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "onMessageReceived: remoteMessage:${remoteMessage.notification}")

        val closeNotification = CloseChannelNotification.from(remoteMessage)
        if (closeNotification != null) {
            // Create and set a pending intent for starting Sample Activity
            val notificationIntent = Intent(this, SampleMainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            val smallIcon = R.mipmap.close_sample_sdk_icon
            val priority = NotificationCompat.PRIORITY_HIGH
            closeNotification.sendNotification(this, notificationIntent, smallIcon, priority)
        } else {
            Log.d(TAG, "remoteMessage received but not a close notification")
            // Custom code to handle own application
        }
    }
}