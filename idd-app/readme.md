# Identify Developer Device

This repo provides a proof-of-concept way that developer devices can be identified from third-party apps.

The app works by listening for app installs in a `BroadcastReceiver`. The receiver can either be defined in the manifest or programmatically created and ran in a `Service`.

Both have their pros and cons.

The manifest method is reliable and doesn't require a running process, but it is easily detectable by reading the manifest.

The service method would require decompiling the app (doable, but a bit harder than just the manifest) in order to be identifiable, but it requires a running process and developers may not be running any other apps when developing, so it wouldn't be as reliable.


This APK will not be put in Google Play.

Code has purposely been put into few classes and I'm not planning on expanding it any further.

See accompanying blog post for more details.