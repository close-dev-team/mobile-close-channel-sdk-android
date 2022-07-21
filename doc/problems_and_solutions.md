# Problems and solutions

## Compile errors

### Adding the close framework

#### "Build was configured to prefer settings repositories over project repositories but repository ‘GitHubPackages’ was added by build file ‘build.gradle’"
You have to place the repository as part of the `settings.gradle`. Please look up the repository section in the `settings.gradle` file and add there the repositories. Remove it from the `build.gradle`
<details>
<summary>example settings.gradle</summary>

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
                username = user
                password = key
            }
        }
    }
}
rootProject.name = "SdkTest"
include ':app'
```
</details>
  

## Run-time errors

### Configuring the API base URL
When setting up the close channel config, if there is an error in the logging, this can be done to resolve it.

#### "Configuration file was not found"

There should be a file called `close_channel.json` in the `(project)/src/main/res/raw directory`. 

![image](./images/SDK_configuration_location.png)

#### "api_base_url property was not found"

In the `close_channel.json` there should be a property called `api_base_url` and it should be filled with the url of the api of the Close SDK
i.e. 
```json
{
    "api_base_url": "https://api.sdk-sandbox.closetest.nl:16443/"
}
```

#### "api_base_url (...) is not a valid url"

There should be a property called 'api_base_url' and it should be filled with the url of the api of the Close SDK. 
Please check the url if it is formatted correctly. i.e. check the url in a browser.

#### More info
For more info about configuration check out the README under section [Step 2: Using the SDK](../README.md#step-2-using-the-sdk).

