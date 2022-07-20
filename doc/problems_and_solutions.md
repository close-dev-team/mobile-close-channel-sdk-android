# Problems and solutions

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
For more info about configuration check out the [README](../README.md) under section Step 2: Using the SDK.

