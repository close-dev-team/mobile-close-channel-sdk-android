# Push notifications

## Preparations

We need your firebase Cloud messaging key. Contact sdk.thecloseapp.com to where you can send us your key.
You should be able to find it in your Firebase > Project Settings > Cloud Messaging > Cloud Messagin API (Legacy) > Server Key (token)

## Registering push notification information

To receive push notifications from Close you need to send the firebase token. We also would want to know if the user granted permission to send push notifications.

This can be done through a call to:

```kotlin
fun registerPushInfo(
    token: String?, 
    permissionGranted: Boolean, 
    onSuccess: (isPushEnabled: Boolean) -> Unit, 
    onFailure: (CloseChannelError) -> Unit? = null
)
```

It's a best practice to do this in the `onNewToken` in your own extended class from `FirebaseMessagingService` .

```kotlin

class YourMessagingService : FirebaseMessagingService() {

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)

        val permissionGranted = (NotificationManagerCompat.from(application).areNotificationsEnabled())
        
        val onSuccess = { isPushEnabled: Boolean ->
            Log.v("Close Channel SDK", "register push - isPushEnabled:${isPushEnabled}")
            Unit
        }
        
        CloseChannelController.getInstance(application).registerPushInfo(refreshedToken, permissionGranted, onSuccess)
    }

}
```

Note: Please make sure you call this function again if permissionGranted is changed for the user. (i.e. User gave permission)


## Showing Close push notifications

When the app is not active, the OS handles the push notifications. When the app is active you need to do that yourself. You can use the `CloseChannelNotification` convenience class to easily distinguish between Close notifications and your own.

If you use `CloseChannelNotification.from(remoteMessage)` it will only return a `CloseNotification`, if it is a push notification from Close, otherwise it returns null. 

```kotlin
class YourMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("Close Channel SDK", "onMessageReceived: remoteMessage:${remoteMessage.notification}")

        val closeNotification = CloseChannelNotification.from(remoteMessage)
        if (closeNotification != null) {
            // Create and set a pending intent for starting Your Activity
            val notificationIntent = Intent(this, YourActivity::class.java) // replace activity with the activity you want to open
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            // Use your own icon to display the push notification
            val smallIcon = R.mipmap.ic_launcher_round // Replace by own icon
            val priority = NotificationCompat.PRIORITY_HIGH
            closeNotification.sendNotification(this, notificationIntent, smallIcon, priority)
        } else {
            Log.d("Close Channel SDK", "remoteMessage received but not a close notification")
            // Custom code to handle own application
        }
    }
```

<details>
    <summary>Example of a full Custom FirebaseMessagingService</summary>
        
```kotlin
class ApplicationMessagingService : FirebaseMessagingService() {

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d("Close Channel SDK", "Refreshed token: $refreshedToken")

        val permissionGranted = (NotificationManagerCompat.from(application).areNotificationsEnabled())
        val onSuccess = { isPushEnabled: Boolean ->
            Log.v("Close Channel SDK", "register push - isPushEnabled:${isPushEnabled}")
            Unit
        }
        CloseChannelController.getInstance(application).registerPushInfo(refreshedToken, permissionGranted, onSuccess)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("Close Channel SDK", "onMessageReceived: remoteMessage:${remoteMessage.notification}")

        val closeNotification = CloseChannelNotification.from(remoteMessage)
        if (closeNotification != null) {
            // Create and set a pending intent for starting Your Activity
            val notificationIntent = Intent(this, YourActivity::class.java) // replace activity with the activity you want to open
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            // Use your own icon to display the push notification
            val smallIcon = R.mipmap.ic_launcher_round // Replace by own icon
            val priority = NotificationCompat.PRIORITY_HIGH
            closeNotification.sendNotification(this, notificationIntent, smallIcon, priority)
        } else {
            Log.d("Close Channel SDK", "remoteMessage received but not a close notification")
            // Custom code to handle own push notifications
        }
    }
}
```
</details>
          

## Handling taps on notification

To handle notifications and open the Messages or Info view you can also can use the `CloseChannelNotification` convenience class. Not only to easily distinguish between Close notifications and your own, but also to check if the push notification is meant for either the Messages or Info view by checking the `openInInfoView` boolean.

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
	
        if (intent != null) {
            val tappedOnCloseNotification = CloseChannelNotification.from(intent)
	    // Is only found, if close notification was found in the intent
	    if (tappedOnCloseNotification != null) {
                val channelId = tappedOnCloseNotification.channelId
                val closeChannelController = CloseChannelController.getInstance(application)
                if (tappedOnCloseNotification.openInInfoView) {
                    closeChannelController.openChannelInfoView(this, channelId)
                } else {
                    closeChannelController.openChannelMessagesView(this, channelId)
                }
            }
        }
    }

```

## Not using the CloseChannelNotification convenience class

If you prefer not to use the CloseChannelNotification class and parse the push notification message yourself, check out the reference documentation

