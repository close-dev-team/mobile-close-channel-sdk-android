# Mobile Close Channel SDK

The Mobile Close Channel SDK allows you to integrate the Close communication platform in your own Android app.

## No buckles needed

We are developers ourselves and we know how frustrating it can be to integrate an SDK. But not this time: we will do our best to make it a fun exercise instead! Pinky promise, this won't be an emotional roller coaster: buckles are not needed!

So, take a coffee ☕️, tea 🫖, or havermelk 🥛 and take my hand to guide you through this in a few easy steps.
If you still run into problems don't hesitate to contact me at renso@thecloseapp.com!

## Pre-requisites and notes

To make an easy start, be sure you have these versions:

* minSdkVersion 23
* targetSdkVersion 30

⚠️ For min sdk version you can only use the same or a higher version (if you need a lower version, please let us know)
⚠️ For target sdk version you can only use the same or a lower version (if you need a higher version, please let us know)

⚠️ tablet are not supported (only phones)

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

* In your build.gradle in your module add our (private) repository to the repositories section

```
    repositories {
        ...
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

* Sync project and build and you are good to go

* You can start using the Close Channel SDK in your code by simply importing it:

```kotlin
import com.starlightideas.close.sdk.CloseChannelController
```

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

The CloseChannelController instance is the one you're going to talk to. Let's first create it.

```kotlin
import com.starlightideas.close.sdk.CloseChannelController
  
    ...
    val closeChannelController = CloseChannelController.getInstance(requireActivity().application)
    ...
```

As it is a singleton instance, you can create and use it in any of your classes.

### Configuring the Close endpoint URL

When you run this, in the console you will see the message: `The API base URL is not set`. That's because the SDK does not know to which Close endpoint it should to talk to. This URL needs to be configured first.

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
