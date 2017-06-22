# Camera-Filters
App that manipulates Android camera feed using OpenCV native library

## Summary

Camera-Filters allows a user to view the devices original camera feed side-by-side with the same feed having gone through image processing. There are two groups of image processing available - colour manipulation and more general image processing - of which the user can pick one from each list to be acted upon the original feed. 

## Camera implementation

The app gets the device's camera feed by using the (deprecated) android.hardware.Camera class. Specifically, it is using an extension of OpenCV's JavaCameraView to handle camera logic and provide frames for processing, although this should probably be taken out and substituted for more direct handling of the camera APIs, as it is only being used for the camera functionality and not the view aspects - it has been left in to save time, but is not necessarily a good solution long term.

## Image processing

Image manipulation has been achieved by using OpenCV Android SDK, with the help of samples provided alongside the SDK. Currently (as of 22/06/2017), the supported image processing types are RGBA, BRGA, greyscale, and canny for the colour processing group, and zoom, pixelise, and posterise for the more general processing group, although more may be added in the future, and by using different libraries. The use of different libraries may strengthen the argument of rewriting the camera functionality to allow for the use of the camera frames as raw byte data and handle it differently for each library.

## Architectural style

The app builds on my own UiWrapper library to provide it's architecture, which is designed to aid in a more easily testable solution. Although I haven't written any tests, I believe the modular design allows for easier digestion of the source code once one becomes familiar with the architecture. That being said, there are aspects of the app that could do with refactoring slightly, which tests would reveal whilst providing support for further refactoring.

## Final notes

To run this app on a device, you must install the appropriate the OpenCV Manager apk for the device's architecture, which are supplied with the OpenCV Android SDK. This can be downloaded here:

[https://sourceforge.net/projects/opencvlibrary/files/opencv-android/3.2.0/]

I hope you enjoy this application and don't find toggling the menu open/closed too annoying. P.S. you can close the menu by using the back button instead of tapping the screen again.
