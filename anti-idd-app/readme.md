# AIDD: Anti Identify Dev Device

This app identifies apps that have the capability to identify developer devices.

(Learn more in my blog post: [Privacy: Programmatically Identifying Developer Devices](http://blog.anonsage.com/2016/03/privacy-programmatically-identifying-dev-device.html))



## Code Design / Architecture

The main entry points are `MainActivity` and `MainReceiver`.

The main code for scanning and showing the list of apps is `MainFragment` with the `ScanAppsTasks` running the long-running code on a background thread.

Clicking on an app in the list shows a custom dialog from `AppModelDetailPage`. Clicking on a notification shows a full-screen Activity from `AppModelDetailPage`. Both of those use the same View from `AppModelDetailPage.getView()`. I chose not to use a dialog-theme Activity because I felt it didn't look as good when coming from a notification.

As a scan is being performed (or caught by receiver), "infected" (IDD-like) apps (package names) are simply saved to a separate `SharedPreferences` file. That way, the next time opening the app will more quickly show all "infected" apps that were found already. To make things simple, just the package name has to be saved because it is relatively quick to dynamically get the rest of the data for the app list. The reason it's slow in the first place is parsing and traversing the manifest for each file. That step is skipped for saved "infected" apps.

All graphics created by using Android Studio image asset tools.



## Further Potential Ideas

- Scan dex/smali files for IDD usages
- Keep track of app version codes/numbers so that they aren't scanned multiple times
- Possibly add more limitations on which packages are shown:
  - Check for data tag's host and package. [More info](http://developer.android.com/guide/topics/manifest/data-element.html)
- In-app package detail page:
  - Use https://github.com/jaredrummler/APKParser to show if app is signed or not
- In package detail page:
  - Show all package watching intents
  - Show all permissions asked for
- Settings page for "Scan all apps":
  - Adjust what intent actions are looked for
  - Option to hide system apps? (Not a priority feature, and maybe they shouldn't be excluded)
- Something to maybe integrate: https://github.com/jaredrummler/AndroidProcesses (low priority)

If you plan on working on any of these above functionality before me, then please first create an issue for it and post your thoughts for how it might be done.



## "Simple" TODOs up for grabs to get pull-request experience (max one per person)

- Add parent Activity support for API 15 for `AppModelDetailPage` (hint: need to edit AndroidManifest.xml)
- Move hardcoded strings to strings.xml (hint: lint checks or alt+enter)
- In `AppModelDetailPage.getView()`:
  - *Emphasize* the "internet" and "running" TextViews when they are true
  - Show the app's icon
- Change action bar to toolbar
- In `MainFragment`, add refresh button to toolbar when list is not empty (there should still be a large button somewhere on the screen if the list is empty). Or, FAB, which is to be hidden when not scrolled to top
- Update `AppModelDetailPage`'s Dialog to something (but not fullscreen) that'll stay shown on rotation
- In `AppModel`, add an attribute that has checked to see if the package requests a permission for getting account info
  - Optionally, integrate this into the `AppModelDetailPage` by the other attributes

Code contributions should generally follow [AOSP code style](https://source.android.com/source/code-style.html).



## TODOs for further research

- Read shared preferences and database files of other devices to see if they are storing a particular app. But, can can be countered by simple obfuscation.
- Explore possibilities to send fake broadcast for `Intent.ACTION_PACKAGE_*`? They are protected by system (aka, only system can send them) and will throw SecurityException if attempted. Sample commented out code already in bottom of MainActivity.



## License

    The MIT License (MIT)

    Copyright (c) 2016 Danial Goodwin

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
