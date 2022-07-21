# Mobile Close Channel SDK (for Android)

The Mobile Close Channel SDK allows you to integrate the Close communication platform in your own Android app.

## No buckles needed

We are developers ourselves and we know how frustrating it can be to integrate an SDK. But not this time: we will do our best to make it a fun exercise instead! Pinky promise, this won't be an emotional roller coaster: buckles are not needed!

So, take a coffee ☕️, tea 🫖, or havermelk 🥛 and take my hand to guide you through this in a few easy steps.
If you still run into problems don't hesitate to contact us via https://sdk.thecloseapp.com/!

## Pre-requisites and notes

To make an easy start, be sure you have these versions:

* minSdkVersion 23
* targetSdkVersion 30

⚠️ For min sdk version you can only use the same or a higher version (if you need a lower version, please let us know)</br>
⚠️ For target sdk version you can only use the same or a lower version (if you need a higher version, please let us know)

Our Close channel SDK works best on phones in only in potrait mode

⚠️ tablet and landscape mode are not supported

### Permissions

No special permissions are needed

### Localisation

The SDK supports `en_GB`, `en_US`, `nl` and `de` localisations. The default localisation is `en_GB`.


# Quick start

## Step 1: Adding the SDK

To add the SDK to your project follow the steps in this section.

### Using Gradle build
---

⛔️ IMPORTANT: The SDK framework is in a private github repository. Please contact Close to get credentials to download the SDK.

---

#### Adding the Close framework
When you have arranged that, then add Close to your Gradle build.

* Add our (private) repository to the repositories section. This could be in the `build.gradle` in your module or in the `settings.gradle` (depending on your gradle version)
* Also add jcenter() if you don't have it yet.

```
    repositories {
        ...
        jcenter()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/close-dev-team/mobile-close-channel-sdk-android")
            credentials {
                username = github_user
                password = github_key
            }
        }
    }
```

* Fill in our credentials at (please contact us to retrieve credentials)
  * github_user - Github user name
  * password - Github password / access key

* In your build.gradle in your module add the SDK dependancy in dependancies section

```
dependencies {
    ...
    implementation "com.thecloseapp.close:close-channel:1.1.0"
}

```
Note: Please check out if you are using the latest version, android studio would probably point out that you can update the version number

* In gradle.properties make sure you enable jetifier on true

```
android.enableJetifier=true
```

* Sync project and build and you are good to go

* You can start using the Close Channel SDK in your code by simply importing it:

```kotlin
import com.starlightideas.close.sdk.CloseChannelController
```

Note: If you have any issues during compilation, please check our [Problems and Solutions](./doc/problems_and_solutions.md#adding-the-close-framework) about adding the close framework


<details>
<summary>Example of a minimal build.gradle</summary>

```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.thecloseapp.sample'

    compileSdk 31

    defaultConfig {
        applicationId "com.thecloseapp.sample"
        minSdk 23
        targetSdk 30
        versionCode 1
        versionName "1.0.0"

        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    buildFeatures {
        viewBinding true
    }

    repositories {
        google()
        jcenter()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/close-dev-team/mobile-close-channel-sdk-android")
            credentials {
                username = github_user // Please replace this by given credentials
                password = github_key // Please replace this by given credentials
            }
        }
    }
}

dependencies {

    implementation 'androidx.core:core-ktx:1.8.0'
    implementation 'androidx.appcompat:appcompat:1.4.2'
    implementation 'com.google.android.material:material:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    implementation "com.thecloseapp.close:close-channel:1.0.4"
}

```

</details>

## Step 2: Using the SDK

The Close SDK needs an endpoint to the backend it should to talk to. This URL needs to be configured first.

### Configuring the Close endpoint URL

You can configure this by following these steps:

* Add a json config file named `close_channel.json` to your res/raw folder
* Add a string property named `api_base_url` with the URL as the value on the top level inside the json

⚠️ For testing purposes you can use the url `https://api.sdk-sandbox.closetest.nl:16443/`, but this should be replaced later with the URL that Close provides to your company.

<details>
  <summary>Example of close_channel.json</summary>

```json
{
  "api_base_url": "https://api.sdk-sandbox.closetest.nl:16443/"
}
```

</details>

### Adding Close Channel Controller
The CloseChannelController instance is the one you're going to talk to. Let's first create it.

```kotlin
import com.starlightideas.close.sdk.CloseChannelController
  
class YourClass {  
    ...
    val closeChannelController = CloseChannelController.getInstance(requireActivity().application)
    ...
}
```

As it is a singleton instance, you can create and use it in any of your classes.

Note: If you have any issues during running, please check our [Problems and Solutions](./doc/problems_and_solutions.md#configuring-the-api-base-url) about configuring the api base url

## Step 3: Registering a user

When the SDK is correctly set up we can continue connecting to the Close platform. This starts with registering a user on our platform.

```kotlin

    private fun registerUser() {
        // This can take a while, so you might want to call it asynchronously
        val closeChannelController = CloseChannelController.getInstance(application)

        val onSuccess = { closeUserId: String ->
            // Save the closeUserId for later use, but for now we just display
            Log.d("Close Channel SDK", "register user with id:${closeUserId}")
            Unit
        }

        val onFailure = { closeChannelError: CloseChannelError ->
            // For now we only log, but it would be good to retry when having no internet
            Log.d("Close Channel SDK", "Error:$closeChannelError")
            Unit
        }

        val uniqueId = "testId" // This should be replaced by a uniqueId for the user or be null
        val nickname = "testNick" // This should be replaced by the nickname of the user or be null

        closeChannelController.registerUser(uniqueId, nickname, onSuccess, onFailure)
    }
```

A `uniqueId` is the id for a user in Close. 
Please make sure this is something a user cannot change him/herself, because then it would not be possible to link to the same data. 
So don't use a phone number or E-mail address. Instead, for example, use an UUID. 
If the value is null a uniqueId will be generated

⚠️ `uniqueId` is optional. If the value is null a unique ID will be generated.

The nickname is the nickname, first name or full name of the user. It is optional so does not have to be specified.

When registerUser is called multiple times, it will return the already existing user. It is not possible to change the uniqueId or to change the nickname with the registerUser call.

Please make sure to read the reference documentation for more information

## Step 4: Adding a channel

After the user is registered you only should add a channel.
Please contact Close for the correct Close code for your app. For now you can use `SDKDEMO`

```kotlin
    private fun addChannel() {
        val closeChannelController = CloseChannelController.getInstance(application)
        val onSuccess = { channel: Channel ->
            Log.d("Close Channel SDK", "Added channel:${channel.name}")
            Unit
        }

        val onFailure = { closeChannelError: CloseChannelError ->
            // For now we only log, but it would be good to retry when having no internet
            Log.d("Close Channel SDK", "Error:$closeChannelError")
            Unit
        }

        val closeCode = "SDKDEMO"
        closeChannelController.addChannel(closeCode, onSuccess, onFailure)
    }
```

>⚠️ Note that you should only add a channel once. When you try to add another channel with the same Close code, you'll receive an error. You can use the `getCloseChannels` function to check if a channel is already available, before adding it. See the section **Tying it all together** below for an example.

## Step 5: Showing a channel

To show a channel, you can simple call the function below to open the last added channel. Be sure to do this on the UI thread.

```kotlin
    private fun openChannel() {
        val closeChannelController = CloseChannelController.getInstance(application)
        closeChannelController.openChannelMessagesView(activity = this)
    }
```

<details>
  <summary>An alternative channel view</summary>

  Besides the channel messages view, which shows messages in chat-like way with text balloons, there is an alternative view which is called the Info view. In this view it is possible to show informational messages, tickets and bought products.

  ```kotlin
    private fun openChannelInfo() {
        val closeChannelController = CloseChannelController.getInstance(application)
        closeChannelController.openChannelMessagesView(activity = this)
    }
  ```

</details>

<details>
  <summary>Opening a specific channel</summary>

Alternatively you can retrieve a list of channels and use the channel ID to open a specific channels

```kotlin
    private fun openFirstChannel() {
        val closeChannelController = CloseChannelController.getInstance(application)
        val onSuccess = { channels: List<Channel> ->
            Log.d("Close Channel SDK", "Number of channels:${channels.size}")
            if (channels.isNotEmpty()) {
                val channel = channels[0]
                closeChannelController.openChannelMessagesView(activity = this, channel.id)
            }
        }

        val onFailure = { closeChannelError: CloseChannelError ->
            // For now we only log, but it would be good to retry when having no internet
            Log.d("Close Channel SDK", "Error:$closeChannelError")
            Unit
        }

        closeChannelController.getChannels(onSuccess, onFailure)
    }
```
</details>

# Tying it all together

As onSuccess callbacks are used to return wether a call succeeded or failed, it is easy to tie everything together:
The goal would be to open the messages channel with only 1 press of a button

* We register a user (when it already exists, it just returns the existing user)
* When that succeeds, we check if there are channels available
  * If so, we open the last added channel
  * If not, we create a channel and open the just created channel

```kotlin
class YourActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYourBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityYourBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.openChannel.setOnClickListener() {
            registerAndAddChannelAndOpen()
        }
    }

    private fun registerAndAddChannelAndOpen() {
        // This can take a while, so you might want to call it asynchronously
        val closeChannelController = CloseChannelController.getInstance(application)

        val onSuccess = { closeUserId: String ->
            // Save the closeUserId for later use, but for now we just display
            Log.d("Close Channel SDK", "register user with id:${closeUserId}")
            getOrAddChannelAndOpen()
        }

        val uniqueId = "testId" // This should be replaced by a uniqueId for the user or be null
        val nickname = "testNick" // This should be replaced by the nickname of the user or be null

        closeChannelController.registerUser(uniqueId, nickname, onSuccess, genericOnFailure)
    }

    private fun getOrAddChannelAndOpen() {
        val closeChannelController = CloseChannelController.getInstance(application)
        val onSuccess = { channels: List<Channel> ->
            Log.d("Close Channel SDK", "Number of channels:${channels.size}")
            if (channels.isEmpty()) {
                addChannelAndOpen()
            } else {
                closeChannelController.openChannelMessagesView(activity = this)
            }
        }

        closeChannelController.getChannels(onSuccess, genericOnFailure)
    }

    private fun addChannelAndOpen() {
        val closeChannelController = CloseChannelController.getInstance(application)
        val onSuccess = { channel: Channel ->
            Log.d("Close Channel SDK", "Added channel:${channel.name}")
            closeChannelController.openChannelMessagesView(activity = this, channel.id)
        }

        val closeCode = "SDKDEMO"
        closeChannelController.addChannel(closeCode, onSuccess, genericOnFailure)
    }

    private val genericOnFailure = { closeChannelError: CloseChannelError ->
        // For now we only log, but it would be good to retry when having no internet
        Log.d("Close Channel SDK", "Error:$closeChannelError")
        Unit
    }
}
```
<details>
<summary>This activity is based on this activity_your.xml</summary>

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/open_channel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="open channel"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
</details>

# Congratulations!

Your app is now integrated with the Close platform! To improve and make it ready for production please check out the sections below.

## Use the correct Close endpoint URL and Close code
Change the `api_base_url` to the correct once you received from Close. Also make sure to use the right Close code. It can happen that the one for a Testing or Staging environment differs from the one on the Production environment.

# Where to continue from here?

## SDK Reference Documentation

The code samples in the sections above have been simplified by not showing all parameters. Make sure you read the [SDK reference documentation](./doc/SDK%20Reference%20Documentation/) for all details.

## Error handling

Additionally, in the code samples above, error handling has been greatly simplified. You should improve on error handling and add retries, [which is explained in detail here](./doc/error_handling.md).

## Push notifications

Messages are meaning nothing if your users don't read them. Be sure you connect
the Close push notifications. It's not hard, we do most of the work for you. [Continue reading here](./doc/push_notifications.md).

## List of channels

If you want to integrate with multiple channels and want to implement an overview of channels, [be sure to checkout this document](./doc/list_of_channels.md)

## Create your own flows

With the Close Builder you can create your own flows to send to your users. Use the account provided by Close to login and start building!

![](https://github.com/close-dev-team/mobile-close-channel-sdk-ios/raw/main/doc/images/builder.png)


## Problems and solutions

When running into problems, please check out [this document](./doc/problems_and_solutions.md) first.

* Report any issues found in the SDK, documentation or sample code [here](https://github.com/close-dev-team/mobile-close-channel-sdk-android/issues)

Note that references to CL-#### in commit messages refer to issues in our private issue tracker.


