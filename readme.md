# Find Dev Devices

It would be possible to programmatically find the likely device(s) of the different app developers..

It's due to the fact that app developers usually install their app many times. And, it's possible to watch for those changes in Android.

Note: The scan can take awhile if there are many apps on the device.



Notes for why this app is useful:
- Possible security vulnerability by targeting devices, through apps
- Paranoid
- Learn something new

- Easier to target apps with few number of devs. Possible privacy issue. For apps with many devs, it could still be targeting, but not as specific as an individual level.

- But, currently this app has limitations. If the code is embedded in another app's Service, then nothing related to seeing the intent has to be added to the Manifest.
  - Though, typically when I'm working on an app, I don't have any other apps running so that wouldn't be a problem. (Caveat: launcher and Google/Android systems)



## TODO

- In package detail page:
  - Show all package watching intents
  - Show all permissions asked for
  - Button to display full manifest? Could do it by sending as Intent to another app to handle.
- Settings page for what "Scan all apps":
   - Adjust what intent actions are looked for
   - Option to hide system apps? (Not a priority feature)

- Possibly create library for 'ListLayout' that takes a List<T> and onClickListener. Should be simple to use and easy to memorize.

- Since developers would be interested in this, have donate options for 1.618, 2.718, 3.14, 6.22, 9.088.
  - Create library to do this easily

- Something to maybe integrate: https://github.com/jaredrummler/AndroidProcesses



## TODO v2
- Possibly add more limitations on which packages are shown:
  - Check for data tag's host and package. [More info](http://developer.android.com/guide/topics/manifest/data-element.html)
- In-app package detail page:
  - Use https://github.com/jaredrummler/APKParser to show if app is signed or not
- Scan popular apps in store.
- Read shared preferences and database files of other devices to see if they are storing a particular app. But, can can be countered by simple obfuscation.
- Send fake broadcast and can see if they are received? Maybe Android system doesn't have that capability




## Third party libraries

- [APKParser](https://github.com/jaredrummler/APKParser): It has minSdkVersion 14, and targets 23



## Blog post

It would be possible to programmatically find the likely device(s) of the different app developers..

It's due to the fact that app developers usually install their app many times. And, it's possible to watch for those changes in Android.


I've created two working proof-of-concept apps: One to programmatically identify developer devices, and one to programmatically identify app that are capable of identifying developer devices.

The identify-dev-device app has not had the internet permission given to it. That way it can be tested by others more safely. In fact, the app requires ZERO permissions in order to function.

For this proof-of-concept version, with people only testing on their own devices, it shows a `Toast` when an app is installed, and it show a notification when an app is installed four times in one day. I felt this was enough times to catch mostly developers and not regular users. And, the count and other configs are easily changeable in `MainReceiver.java`. Devices for CI (Continuous Integration) may get 10s or 100s of app installs a day, and those CI devices will either be shared (and likely no other running apps) or personally-used devices.

The anti-identify app will identify itself as being capable of identifying developer devices because it has the `BroadcastReceiver` watching out for the app installs. The app will provide a notification anytime an app is installed that can also watch app installs.

On the main page of this app, you can force a scan of all apps.

You'll also happen to notice many Google and system apps watch for app installations. Probably too many, but definite conclusions can't be drawn from the information provided by this app.

I also use a file explorer app, which recently introduced a new feature to identify if an install app has dangerous permissions. But, it also has internet permissions and is closed-source.


The anti-identify app is on Google Play.



One way to possibly help with privacy now is to sent a random amount of "fake" broadcasts for the message, and bias lower for developers. But, that could potentially be counted by requiring system permission for the receiver.



I would like to see the permission required in Manifest so that it is more robust when scanning apps.

I hope no apps are actively trying to identify developer devices, and now we have an app that can check.

A "prefix" permission? For Google they may only want to packages that start with 'com.google' maybe.
