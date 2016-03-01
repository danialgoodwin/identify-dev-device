# Programmatically Identify Developer Devices

Apps can programmatically identify likely device(s) of the different app developers.

It's due to the fact that app developers usually install their app many times. And, it's possible to watch for those app installs in Android.

Also, implementing an app that takes advantage of this requires zero permissions.

So, this repo has two apps: One to programmatically identify dev devices, and one to identify those kind of apps.

Imagine, if you are an app developer, then your device(s) can be identified and have special code ran for them and nobody else will experience the same issue or app changes.

Notes for why this app is might be useful (but still shouldn't be used):

- Learn something new: More approaches to breaching privacy
- Show resume to app developer(s) at a specific company
- Run different app code for other developers, or for non-developers
- Possible security vulnerability by targeting devices, through apps

- Easier to target companies/apps with few number of devs. Possible privacy issue. For apps with many devs, it could still be targeting, but not as specific as an individual level (unless app has GET_ACCOUNTS permission).



## Identify Dev Device app

More info in another readme in the identify-app module's directory.



## AIDD: Anti Identify Dev Device app

This app identifies apps that have the capability to track app installs, thus dev devices.

This is achieved by reading the AndroidManifest file for the apps and looking for a BroadcastReceiver that has the Intent action for watching package changes (add/replace/remove).

More info in another readme for this module.



## TODOs for further research

- Scan popular apps in store.



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







Currently this app has limitations. If the code is embedded in another app's Service, then nothing related to seeing the intent has to be added to the Manifest.
  - Though, typically when I'm working on an app, I don't have any other apps running so that wouldn't be a problem. (Caveat: launcher and Google/Android systems)
