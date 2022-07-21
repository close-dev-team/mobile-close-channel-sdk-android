# Error handling

Functions in the SDK that can fail have two callbacks.

* success
* failure

These are respectively called when then call succeeded or failed. In case of failure a [CloseChannelError](https://close-dev-team.github.io/mobile-close-channel-sdk-android/-mobile%20-close%20-channel%20-s-d-k/com.starlightideas.close.sdk/-close-channel-error/index.html) object is passed which contains details of the error.

## Retrying calls
It is good practice to retry the calls in case of the `API_CALL_FAILED_SERVER_UNREACHABLE` error. Best to first check if the internet is down, and retry if internet is back up.

## User not valid
If you receive an `API_CALL_FAILED_USER_NOT_VALID` it can mean 2 things 
Either the user was never registers. If so, please register the user first, before doing any other calls
Or the user's token is not valid anymore. **The token never expires but becomes invalid when the unique id is being re-used**. 
This can happen when the user reinstalls the app or installs the app on another device.

## Internal SDK Error
If you receive an internal SDK error it can mean there is something wrong in scenario you are trying to implement , or that we made a booboo. Contact us for help.

## Other errors

All errors that can be reported are explained [here](./SDK%20Reference%20Documentation/enums/CloseChannelController.CloseChannelError.md)
