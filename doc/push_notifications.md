# Push notifications

## Preparations

We need your firebase Cloud messaging key. Contact sdk.thecloseapp.com to where you can send us your key.
You should be able to find it in your Firebase > Project Settings > Cloud Messaging > Cloud Messagin API (Legacy) > Server Key (token)

## Registering

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



It's a best practice to do this in the `onNewToken` in the FirebaseMessagingService .

```kotlin


```

Note: Please make sure you call this function again if permissionGranted is changed for the user. (i.e. User gave permission)



## Not using the CloseChannelNotification convenience class

If you prefer not to use the CloseChannelNotification class and parse the push notification message yourself, check out the reference documentation

