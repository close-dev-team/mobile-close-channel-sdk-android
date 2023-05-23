# Mobile Close Channel SDK (for Android)

The Mobile Close Channel SDK allows you to integrate the Close communication platform in your own Android app.

## No buckles needed

We are developers ourselves and we know how frustrating it can be to integrate an SDK. But not this time: we will do our best to make it a fun exercise instead! Pinky promise, this won't be an emotional roller coaster: buckles are not needed!

So, take a coffee ☕️, tea 🫖, or havermelk 🥛 and take my hand to guide you through this in a few easy steps.
If you still run into problems don't hesitate to contact us via https://sdk.thecloseapp.com/!

## Pre-requisites and notes


### What you will need from us to start integration
The following information you will need from us to start integrating the Close Channel SDK
* github access token to download the library from private repository
* close_channel.json
   * That includes API base url and API access token (values to access our sandbox environment can be found below)
* closeCode for adding a channel (or use SDKDEMO to start testing)

### Android SDK version
To make an easy start, be sure you have these versions:

* minSdkVersion 21
* targetSdkVersion 33

⚠️ For min sdk version you can only use the same or a higher version (if you need a lower version, please let us know)</br>
Target sdk version is currently the latest. You can only use the same or a lower version (if you need a higher version, please let us know)


### Permissions

No special permissions are needed

### Localisation

The SDK supports `en_GB`, `en_US`, `nl` and `de` localisations. The default localisation is `en_GB`.

### Form factor and Orientation

⚠️ tablet form factor and landscape mode are not supported</br>
Our Close channel SDK works best on phones in only in portrait mode

# Quick start

Stop ⏹! Start ▶️ with a sample

It is recommended to first start with the [Mobile Close Channel SDK Sample](./sample/CloseChannelSample/) which showcases how to use the SDK and the different features it provides. It talks to a test environment that can be used to test with.

When you've done that you can come back to this page to continue the wonderful journey of integrating Close in your own app.

## Step 1: Adding the SDK

To add the SDK to your project follow the steps in this section.

### Using Gradle build
---

⛔️ IMPORTANT: The SDK framework is in a private github repository. Please contact Close to get credentials to download the SDK.

---

#### Adding the Close framework
When you have arranged that, then add Close to your Gradle build.

* Add our (private) repository to the repositories section. 
    * This could be in the `build.gradle` in your module under section android (for older projects)
    * Or this could be in the `settings.gradle` under section dependencyResolutionManagement (for new projects)
* Also add jcenter() if you don't have it yet.

```
    repositories {
        ...
        jcenter()
        maven {
            name = "ClosePackages"
            url = uri("https://maven.pkg.github.com/close-dev-team/mobile-close-channel-sdk-library-android")
            credentials {
                username = "github_user"
                password = "github_access_token"
            }
        }
    }
```

* Fill in our credentials at (please contact us to retrieve credentials)
  * github_user - Github user name (can be any user name)
  * password - Github access token

* In your build.gradle in your module add the SDK dependancy in dependancies section

```
dependencies {
    ...
    implementation "com.thecloseapp.close:close-channel:1.5.5"
}

```
Note: Please check out if you are using the latest version, android studio would probably point out that you can update the version number

* Sync project and build and you are good to go

* You can start using the Close Channel SDK in your code by simply importing it:

```kotlin
import com.thecloseapp.close.channel.sdk.CloseChannelController
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

    compileSdk 33

    defaultConfig {
        applicationId "com.thecloseapp.sample"
        minSdk 21
        targetSdk 33
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
                username = "github_user" // User is not relevant
                password = "github_key" // Please replace this by given credentials
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

In new project settings.gradle is used to add resources

<details>
<summary>Example of a minimal settings.gradle</summary>

```
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/close-dev-team/mobile-close-channel-sdk-android")

            credentials {
                username = "github_user" // User is not relevant
                password = "github_key" // Please replace this by given credentials
            }
        }
    }
}
rootProject.name = "Close Channel Sample"
include ':app'

```

</details>


## Step 2: Using the SDK

The Close SDK needs an endpoint to the backend it should to talk to. This URL needs to be configured first.

### Configuring the Close endpoint URL

You can configure this by following these steps:

* Add a json config file named `close_channel.json` to your res/raw folder
    * Add a string property named `api_base_url` with the URL as the value on the top level inside the json
    * Add a string property named `api_access_token` with the api access token the top level inside the json

⚠️ For testing purposes you can use the api_base_url `https://api.sdk-sandbox.closetest.nl:16443/` and the api_access_token `sdk-sandbox-access-token`, but this should be replaced later with the URL that Close provides to your company.

<details>
  <summary>Example of close_channel.json</summary>

```json
{
  "api_base_url": "https://api.sdk-sandbox.closetest.nl:16443/",
  "api_access_token": "sdk-sandbox-access-token"
}
```
</details>

See also [close_channel.json in the sample app](./sample/CloseChannelSample/app/src/main/res/raw/close_channel.json)

### Adding Close Channel Controller
The CloseChannelController instance is the one you're going to talk to. Let's first create it.

```kotlin
import com.thecloseapp.close.channel.sdk.CloseChannelController
  
class YourClass {  
    ...
    val closeChannelController = CloseChannelController.getInstance(application)
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

Please make sure to read the [reference documentation](https://close-dev-team.github.io/mobile-close-channel-sdk-android/-mobile%20-close%20-channel%20-s-d-k/com.thecloseapp.close.channel.sdk/-close-channel-controller/register-user.html) for more information

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

 There are 2 ways of showing the channel
 * Channel messages view. This shows messages in chat-like way with text balloons, ordered by date/time of sending
 * Channel info view. This shows informational messages, tickets and bought products, ordered by pre defined order

<details>
  <summary>Showing the channel info view</summary>
  
   The display the info view use the following code snippet

  ```kotlin
    private fun openChannelInfo() {
        val closeChannelController = CloseChannelController.getInstance(application)
        closeChannelController.openChannelInfoView(activity = this)
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
   
### Display a channel in your own view
  
Alternatively you can retrieve the fragment with the channel, to display on your own activity or other view (i.e. tab)
* Note: You should always use a large part (80% or more) of your screen to display the close channel correctly

  
<details>
  <summary>Display channel fragment on container view</summary>

```kotlin
    private fun openChannelInPage() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Default) {
                val closeChannelController = CloseChannelController.getInstance(requireActivity().application)

                withContext(Dispatchers.Main) {
                    val onSuccess = { fragment: Fragment ->
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.messages_container_view, fragment)
                                ?.commit()
                        }
                    }

                    val onFailure = { closeChannelError: CloseChannelError ->
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            Toast.makeText(context, "Error:$closeChannelError", Toast.LENGTH_LONG).show()
                        }
                    }

                    closeChannelController.getChannelMessagesFragment(
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                }
            }
        }
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

        
## Important note
    
When you use the callback functions onSuccess and onFailure and you want to update the UI. Make sure you check the lifecycle, (you could check for onStop), to prevent handling callbacks when the activity or fragment is not running anymore. Example:

```kotlin
   val onSuccess = { channelList: List<Channel> ->
       if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
           displayChannelList(binding.channelList, channelList)
       }
   }
```
    
# Where to continue from here?

## Documentation section

Further documentation on specific tasks see: [documentation section](./doc/).

## SDK Reference Documentation

The code samples in the sections above have been simplified by not showing all parameters. Make sure you read the [SDK reference documentation](https://close-dev-team.github.io/mobile-close-channel-sdk-android/-mobile%20-close%20-channel%20-s-d-k/com.thecloseapp.close.channel.sdk/index.html) for all details.

## Error handling

Additionally, in the code samples above, error handling has been greatly simplified. You should improve on error handling and add retries, [which is explained in detail here](./doc/error_handling.md).

## Push notifications

Messages are meaning nothing if your users don't read them. Be sure you connect
the Close push notifications. It's not hard, we do most of the work for you. [Continue reading here](./doc/push_notifications.md).

## List of channels

Check this out if you want to [integrate with multiple channels](./doc/list_of_channels.md)

## Open a channel in a tab

You can also [open a channel in your own tab or other UI element](./doc/open_channel_in_tab.md)

## Create your own flows

With the Close Builder you can create your own flows to send to your users. Use the account provided by Close to login and start building!

In the following video we explain how to send a simple text messages   

[![Close Builder sending text message](//github.com/close-dev-team/mobile-close-channel-sdk-android/assets/108068516/9359ea14-233a-4039-930a-296c2c340b76)](//www.youtube.com/watch?v=AkxLAJ0NQ8o&list=PLhVIgEB_sAkYYpItjNWPjr2HVsPoLNxxP&index=2) "Close Builder sending text message")

https://www.youtube.com/watch?v=AkxLAJ0NQ8o&list=PLhVIgEB_sAkYYpItjNWPjr2HVsPoLNxxP&index=2

https://www.youtube.com/watch?v=AkxLAJ0NQ8o
  
(in the youtube channel you also find other tutorials)  
    
## Problems and solutions

When running into problems, please check out [this document](./doc/problems_and_solutions.md) first.

* Report any issues found in the SDK, documentation or sample code [here](https://github.com/close-dev-team/mobile-close-channel-sdk-android/issues)

Note that references to CL-#### in commit messages refer to issues in our private issue tracker.


