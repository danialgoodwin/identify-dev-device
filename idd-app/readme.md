# IDD: Identify Developer Device

This module provides a proof-of-concept way that developer devices can be identified from third-party apps.

(Learn more in my blog post: [Privacy: Programmatically Identifying Developer Devices](http://blog.anonsage.com/2016/03/privacy-programmatically-identifying-dev-device.html))



Code has purposely been put into few classes for simplicity and I'm not planning on expanding it any further.




## Code Design / Architecture

The main entry points are `MainActivity` and `MainReceiver`.

The receiver records all app installs and keeps track of install counts per day in `MainPrefs`. For simplicity, data is stored as a key-value pair with the key being package name appended with day, and the value being number of installs on that day. A limitation in the there is that it uses UTC time rather than device locale.

Opening the app shows all the data that has been saved.

The `MainService` dynamically creates a receiver. A limitation introduced is that it will stop itself if the task is swiped off the recent tasks list. And, it shows a foreground notification, even though that isn't strictly necessary for a `Service` to run in the background.



## License

It might eventually become a GNU GPL version with an added stipulation that the code isn't put in any device that the isn't the developer's actively used device in arm's reach. Using De Morgan's law, we could simply that sentence half to just being put on dev's active device in arm's reach.
