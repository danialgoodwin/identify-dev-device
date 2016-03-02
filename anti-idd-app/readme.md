# AIDD: Anti Identify Dev Device

(Learn more in my blog post: [Privacy: Programmatically Identifying Developer Devices](http://blog.anonsage.com/2016/03/privacy-programmatically-identifying-dev-device.html))





## "Simple" TODOs up for grabs to get pull-request experience (max one per person)

- Add parent Activity support for API 15 for `AppModelDetailPage` (hint: just need to edit AndroidManifest.xml)
- Move hardcoded strings to strings.xml (hint: lint checks or alt+enter)
- In `AppModelDetailPage`, *emphasize* the "internet" and "running" TextViews when they are true
- In `MainFragment`, add refresh button to toolbar when list is not empty (there should still be a large button somewhere on the screen if the list is empty)
- Update `AppModelDetailPage`'s Dialog to something that'll stay shown on rotation (perhaps Fragment or FragmentDialog)
- In `AppModel`, add an attribute that has checked to see if the package requests a permission for getting account info
  - Optionally, integrate this into the `AppModelDetailPage` by the other attributes

Code contributions should generally follow [AOSP code style](https://source.android.com/source/code-style.html).



## Further Potential Ideas

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

- Something to maybe integrate: https://github.com/jaredrummler/AndroidProcesses



## TODOs for further research

- Read shared preferences and database files of other devices to see if they are storing a particular app. But, can can be countered by simple obfuscation.
- Send fake broadcast and can see if they are received? Maybe Android system doesn't have that capability. It could work if this isn't a system broadcast
