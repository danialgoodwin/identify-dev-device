# Programmatically Identify Developer Devices

Apps can programmatically identify likely device(s) of app developers.

It's due to the fact that app developers usually install their app many times. And, it's possible to watch for those app installs in Android.

Also, implementing an app that takes advantage of this requires zero permissions.

So, this repo has two apps: One to programmatically identify dev devices, and one to identify those kind of apps and stop them.

Imagine, if you are an app developer, then your device(s) can be identified and have special code ran for them and nobody else will experience the same issue or app changes.

Notes for why this app might be useful (but still shouldn't be used):

- Learn something new: More approaches to breaching privacy
- Show resume to app developer(s) at a specific company
- Run different app code for other developers, or for non-developers
- Possible security vulnerability by targeting devices, through apps

- Easier to target companies/apps with few number of devs. Possible privacy issue. For apps with many devs, it could still be targeting, but not as specific as an individual level (unless app has GET_ACCOUNTS permission).


(Learn more in my blog post: [Privacy: Programmatically Identifying Developer Devices](http://blog.anonsage.com/2016/03/privacy-programmatically-identifying-dev-device.html))



## IDD: Identify Dev Device app

More info in another readme in the [identify-app module's directory](https://github.com/danialgoodwin/identify-dev-device/tree/master/idd-app).



## AIDD: Anti Identify Dev Device app

This app identifies apps that have the capability to track app installs, thus dev devices.

This is achieved by reading the AndroidManifest file for the apps and looking for a BroadcastReceiver that has the Intent action for watching package changes (add/replace/remove).

More info in another readme in the [anti-idd-app module's directory](https://github.com/danialgoodwin/identify-dev-device/tree/master/anti-idd-app).



## TODOs for further research

- Scan popular apps in store.
